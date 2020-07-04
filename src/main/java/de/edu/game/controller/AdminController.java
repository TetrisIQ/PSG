package de.edu.game.controller;

import de.edu.game.config.UserService;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.controller.responses.MapResponse;
import de.edu.game.exceptions.GameAlreadyStartedException;
import de.edu.game.exceptions.NotAuthorizedException;
import de.edu.game.model.*;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.MapRepository;
import de.edu.game.repositorys.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for Administration
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:8080")
@Log4j2
@EnableScheduling
public class AdminController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Starts the Game
     *
     * @throws GameAlreadyStartedException When the game has already started
     * @throws NotAuthorizedException      When the user has no privilege to use this endpoint
     */
    @PutMapping("/start")
    @ResponseStatus(code = HttpStatus.OK)

    public void startGame() throws GameAlreadyStartedException, NotAuthorizedException {
        hasAuthority();
        try {
            Game game = gameRepository.getTheGame();
            if (game.startGame(gameRepository)) {
                log.info("Game Started with {} player", game.getUsers().size());
                mapRepository.save(game.getMap());
                gameRepository.save(game);
                log.info(game.getMap().toString());
            } else {
                //game is already started
                log.warn("Game is already started. Cannot start the Game");
                throw new GameAlreadyStartedException();
            }
        } catch (IndexOutOfBoundsException ex) {
            log.warn("No Player joined the Game game cannot Started");
            System.exit(-1);
        }
    }

    /**
     * Get the hole map as Response Object
     *
     * @return A Response of the @{@link Map}
     */
    public MapResponse getMap() {
        try {
            Map map = mapRepository.getTheMap();
            return new MapResponse(map);
            //return map;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Get the hole map as Response Object, including an Update message
     *
     * @param message The update message
     * @return A Response of the {@link Map}
     */
    public MapResponse getMap(String message) {
        try {
            Map map = mapRepository.getTheMap();
            return new MapResponse(map, message);
            //return map;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Try to Spawn every 20 seconds new @{@link Asteroid} on the Map
     */
    @Scheduled(fixedDelay = 20000) // every 20 seconds
    public void spawnAsteroids() {
        try {
            Map map = mapRepository.getTheMap();
            // check if the maximum amount of Asteroids are on the Map
            if (map.countAllAsteroids() >= ConfigLoader.shared.getAsteroid().getMaxAsteroids()) {
                return;
            }
            map.spawnAsteroids(new Dice("1w3").throwDice());
            mapRepository.save(map);
        } catch (IndexOutOfBoundsException e) {
            // pass, because the map is not present aka. the game is not started
        }
    }


    /**
     * Test every second if one player has win <br>
     * A player wins, if he is the only left player in the Game
     */

    @Scheduled(fixedDelay = 1000)
    private void testIfWin() {
        Game game = gameRepository.getTheGame();
        List<User> users = game.getUsers();
        if (users.size() == 1 && game.getState().isGameStarted()) {
            // if there is only one player the game is over and the user has win the game
            log.info("#########################################################################");
            log.info(users.get(0).getUsername() + " win the Game");
            log.info("#########################################################################");
            game.getState().nextState();
            gameRepository.save(game);
            Thread.currentThread().interrupt();
        }
    }

    @PostMapping("/debug")
    public void debug() {
        Game game = gameRepository.getTheGame();
        game.getUsers().forEach(u -> u.getMeepleList().forEach(m -> m.setHasMoved(false)));
        userRepository.saveAll(game.getUsers());
        gameRepository.save(game);

    }

    /**
     * Get Information about the hole Map, Used my the <a href="https://github.com/TetrisIQ/PSG-MapViewer">Map-Viewer</a>
     *
     * @return Result with the Response Object of the @{@link Map} if there is an update on it. <br>
     * Or an Update after an Timeout of 100.000 millSec
     * @throws NotAuthorizedException When the user has no privilege to use this endpoint
     */
    @GetMapping("/map")
    public DeferredResult<MapResponse> getMapIfUpdates() throws NotAuthorizedException {
        hasAuthority();
        Long timeOutInMilliSec = 100000L;
        DeferredResult<MapResponse> deferredResult = new DeferredResult<>(timeOutInMilliSec, getMap());
        CompletableFuture.runAsync(() -> {
            try {
                //Long pooling task;If task is not completed within 100 sec timeout response return for this request
                if (new MapViewerMessageUpdate().hasUpdates()) {
                    deferredResult.setResult(getMap(MapViewerMessageUpdate.message));
                    MapViewerMessageUpdate.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return deferredResult;
    }

    /**
     * This is a temporary workaround, until I get the Spring security annotations working <br>
     * This Throws en exception if the user has not the admin or root role
     *
     * @throws NotAuthorizedException When the user has no privilege to use this endpoint
     */
    private void hasAuthority() throws NotAuthorizedException {
        Optional<User> user = userService.currentUser();
        if (user.isPresent()) {
            if (!(user.get().getRole().equals("admin") || user.get().getRole().equals("root"))) {
                throw new NotAuthorizedException();
            }
        }
    }

}




