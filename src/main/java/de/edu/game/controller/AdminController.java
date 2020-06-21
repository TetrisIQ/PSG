package de.edu.game.controller;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.controller.responses.MapResponse;
import de.edu.game.exceptions.GameAlreadyStartedException;
import de.edu.game.model.*;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.MapRepository;
import de.edu.game.repositorys.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:8080")
@Log
@EnableScheduling
public class AdminController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MapRepository mapRepository;

    @PutMapping("/start")
    @ResponseStatus(code = HttpStatus.OK)
    //@PreAuthorize("hasRole('root')")
    public void startGame() throws GameAlreadyStartedException {
        //TODO: Make Authorization only for Root/Admin account.
        Game game = gameRepository.getTheGame();
        if (game.startGame(gameRepository)) {
            log.info("Game Started with " + game.getUsers().size() + " player");
            mapRepository.save(game.getMap());
            gameRepository.save(game);
            log.info(game.getMap().toString());
        } else {
            //game is already started
            log.warning("Game is already started. Cannot start the Game");
            throw new GameAlreadyStartedException();
        }
    }

    @GetMapping("/map")
    public MapResponse getMap() {
        try {
            Map map = mapRepository.getTheMap();
            return new MapResponse(map);
            //return map;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Scheduled(fixedDelay = 20000) // every 20 seconds
    public void spawnAsteroids() {
        try {
            Map map = mapRepository.getTheMap();
            // check if the maximum amount of Asteroids are on the Map
            if (map.findAllAsteroids() >= ConfigLoader.shared.getAsteroid().getMaxAsteroids()) {
                return;
            }
            map.spawnAsteroids(new Dice("1w3").throwDice());
            mapRepository.save(map);
        } catch (IndexOutOfBoundsException e) {
            // pass, because the map is not present aka. the game is not started
        }
    }


    @GetMapping("/update")
    public DeferredResult<String> isMyTurn() throws TimeoutException {
        Long timeOutInMilliSec = 100000L;
        DeferredResult<String> deferredResult = new DeferredResult<>(timeOutInMilliSec, "Update");
        CompletableFuture.runAsync(() -> {
            try {
                //Long pooling task;If task is not completed within 100 sec timeout response return for this request
                if(new MapViewerMessageUpdate().hasUpdates()) {
                    deferredResult.setResult(MapViewerMessageUpdate.message);
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
     * @throws NotAuthorizedException
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




