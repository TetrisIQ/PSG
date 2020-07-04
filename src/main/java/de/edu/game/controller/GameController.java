package de.edu.game.controller;

import de.edu.game.config.UserService;
import de.edu.game.controller.responses.FieldResponse;
import de.edu.game.exceptions.*;
import de.edu.game.model.*;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.MapRepository;
import de.edu.game.repositorys.MeepleRepository;
import de.edu.game.repositorys.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for most of the game logic
 */
@RestController
@RequestMapping("/game")
@Log4j2
public class GameController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private MeepleRepository meepleRepository;

    @Autowired
    private UserService userService;

    /**
     * Get information about the map <br>
     * based on the own meeple positions
     *
     * @return A set of Response Objects from @{@link Field}
     */
    @GetMapping
    public Set<FieldResponse> getMapInformation() {
        Game game = gameRepository.getTheGame();
        User user = userService.currentUser().get();
        Set<FieldResponse> ret = new HashSet<>();
        if (user.myTurn()) {
            game.getFieldInfo(user).forEach(f -> ret.add(new FieldResponse(user.getUsername(), f)));
        }
        return ret;
    }

    /**
     * Checks if the current user is on turn <br>
     * This is an <b>Blocking</b> call!
     *
     * @param userRepository The {@link UserRepository}
     * @param id             The id ot the {@link User}
     * @return True if the user is on Turn.
     */
    private boolean isMyTurn(UserRepository userRepository, int id) {
        while (true) {
            if (userRepository.findById(id).get().myTurn()) {
                return true;
            }
        }
    }

    /**
     * Endpoint that checks if the user is on Turn
     *
     * @return Result with the Response of a String, if the user is on Turn. <br>
     * Or an Error message after an Timeout of 100.000 millSec
     */
    @GetMapping("/myturn")
    public DeferredResult<HttpStatus> isMyTurn() {
        Long timeOutInMilliSec = 100000L;
        User user = userService.currentUser().get();
        DeferredResult<HttpStatus> deferredResult = new DeferredResult<>(timeOutInMilliSec, HttpStatus.REQUEST_TIMEOUT);
        CompletableFuture.runAsync(() -> {
            try {
                //Long pooling task;If task is not completed within 100 sec timeout response return for this request
                if (this.isMyTurn(userRepository, user.getId())) {
                    deferredResult.setResult(HttpStatus.OK);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return deferredResult;
    }

    /**
     * Endpoint for ending your Turn
     *
     * @throws NotYourTurnException If it was not your turn
     */
    @PutMapping("/endturn")
    @ResponseStatus(value = HttpStatus.OK)
    public void finishTurn() throws NotYourTurnException {
        User user = userService.currentUser().get();
        Game game = gameRepository.getTheGame();
        if (user.finishTurn()) {
            User next = game.nextPlayer(gameRepository);
            MapViewerMessageUpdate.send(next.getUsername() + "'s turn");
            next.next();
            userRepository.save(user);
            /*try {
                Thread.currentThread().sleep(ConfigLoader.shared.getTimeAfterRound());
            } catch (InterruptedException e) {
                log.warn("Thread where interrupted, Not time to wait");
            }*/
            userRepository.save(next);
            gameRepository.save(game);
        } else {
            throw new NotYourTurnException();
        }
    }


    /**
     * Endpoint for moving a @{@link AbstractMeeple} to an adjacent X / Y coordinate
     *
     * @param meepleId The id of the meeple, witch should move
     * @param x        X coordinate
     * @param y        Y coordinate
     * @throws SpaceStationCannotMoveException If the meeple is an SpaceStation it cannot move
     * @throws HasAlreadyMovedException        If the meeple has already moved this round, it cannot move again
     * @throws CannotMoveButIAttackException   If the meeple ({@link Starfighter}) has attack an other meeple, but don't destroy it. It cannot move, and cannot move again this round
     * @throws CannotMoveException             The meeple cannot move to this point, but can move to another this round
     * @throws StorageFullException            The meeple (@{@link Transporter}) cannot mine more energy, because the storage is full
     * @throws CannotAttackOwnMeeplesException The meeple (@{@link Starfighter}) cannot attack own meeples, but can move to an other field this round
     * @throws CannotMineException             The meeple (@{@link Transporter}) cannot mine here
     */
    @PutMapping("/{meepleId}/{x}/{y}")
    public void move(@PathVariable int meepleId, @PathVariable int x, @PathVariable int y) throws SpaceStationCannotMoveException, HasAlreadyMovedException, CannotMoveButIAttackException, CannotMoveException, StorageFullException, CannotAttackOwnMeeplesException, CannotMineException {
        Optional<AbstractMeeple> meeple = meepleRepository.findById(meepleId);
        Optional<User> loggedIn = userService.currentUser();
        Map map = mapRepository.getTheMap();
        if (loggedIn.isPresent() && meeple.isPresent() && loggedIn.get().myTurn()) {
            if (meeple.get().move(map, map.findCoordinate(x, y))) {
            } else {
                throw new CannotMoveException();
            }
            meepleRepository.save(meeple.get());
            mapRepository.save(map);
            userRepository.save(loggedIn.get()); // save if points were added
        }
    }

    /**
     * The {@link SpaceStation} build a new {@link Starfighter}
     *
     * @throws NotEnoughEnergyException If the {@link SpaceStation} has not enough energy to build a {@link Starfighter}
     */
    @PostMapping("/build/starfighter")
    public void buildStarfighter() throws NotEnoughEnergyException {
        Map map = mapRepository.getTheMap();
        User user = userService.currentUser().get();
        if (user.getSpaceStation().spawnStarfighter(map)) {
            mapRepository.save(map);
            userRepository.save(user);
        } else {
            throw new NotEnoughEnergyException();
        }
    }

    /**
     * The {@link SpaceStation} build a new {@link Transporter}
     *
     * @throws NotEnoughEnergyException If the {@link SpaceStation} has not enough energy to build a {@link Transporter}
     */
    @PostMapping("/build/transporter")
    public void buildSTransporter() throws NotEnoughEnergyException {
        Map map = mapRepository.getTheMap();
        User user = userService.currentUser().get();
        if (user.getSpaceStation().spawnTransporter(map)) {
            mapRepository.save(map);
            userRepository.save(user);
        } else {
            throw new NotEnoughEnergyException();
        }
    }
}
