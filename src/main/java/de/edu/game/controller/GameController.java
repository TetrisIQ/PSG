package de.edu.game.controller;

import de.edu.game.config.UserService;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.controller.responses.FieldResponse;
import de.edu.game.exceptions.CannotMoveException;
import de.edu.game.exceptions.NotEnoughEnergyException;
import de.edu.game.exceptions.NotYourTurnException;
import de.edu.game.model.*;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.MapRepository;
import de.edu.game.repositorys.MeepleRepository;
import de.edu.game.repositorys.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    @GetMapping
    public Set<FieldResponse> getMapInformation() throws InterruptedException {
        Game game = gameRepository.getTheGame();
        User user = userService.currentUser().get();
        Set<FieldResponse> ret = new HashSet<>();
        if (user.myTurn()) {
            game.getFildInfo(user).forEach(f -> ret.add(new FieldResponse(user.getUsername(), f)));
        }
        return ret;
    }

    private boolean isMyTurn(UserRepository userRepository, int id) throws InterruptedException {
        boolean stop = false;
        while (!stop) {
            if (userRepository.findById(id).get().myTurn()) {
                stop = true;
                return true;
            }
            TimeUnit.SECONDS.sleep(2);
        }
        return false;
    }

    @GetMapping("/myturn")
    public DeferredResult<HttpStatus> isMyTurn() throws TimeoutException {
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
            try {
                Thread.currentThread().sleep(ConfigLoader.shared.getTimeAfterRound());
            } catch (InterruptedException e) {
                log.warn("Thread where interrupted, Not time to wait");
            }
            userRepository.save(next);
        } else {
            throw new NotYourTurnException();
        }
    }

    @PutMapping("/{meepleId}/{x}/{y}")
    @SneakyThrows
    public void move(@PathVariable int meepleId, @PathVariable int x, @PathVariable int y) {
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

    @PostMapping("/build/starfighter")
    public void buildStarfighter() throws NotEnoughEnergyException {
        Map map = mapRepository.getTheMap();
        User user = userService.currentUser().get();
        if (user.getSpaceStation().spawnStarfighter(map, user)) {
            mapRepository.save(map);
            userRepository.save(user);
        } else {
            throw new NotEnoughEnergyException();
        }
    }

    @PostMapping("/build/transporter")
    public void buildSTransporter() throws NotEnoughEnergyException {
        Map map = mapRepository.getTheMap();
        User user = userService.currentUser().get();
        if (user.getSpaceStation().spawnTransporter(map, user)) {
            mapRepository.save(map);
            userRepository.save(user);
        } else {
            throw new NotEnoughEnergyException();
        }
    }
}
