package de.edu.game.controller;

import de.edu.game.config.UserService;
import de.edu.game.controller.responses.FieldResponse;
import de.edu.game.exceptions.CannotMoveException;
import de.edu.game.exceptions.HasAlreadyMovedException;
import de.edu.game.exceptions.NotYourTurnException;
import de.edu.game.exceptions.SpaceStationCannotMoveException;
import de.edu.game.model.*;
import de.edu.game.repositorys.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/game")
@Log
public class GameController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private MeepleRepository meepleRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    private Set<FieldResponse> getMapInformation() throws InterruptedException {
        Game game = gameRepository.getTheGame();
        User user = userService.currentUser().get();
        Set<FieldResponse> ret = new HashSet<>();
        if (user.myTurn()) {
            Set<Field> test = game.getFildInfo(user);
            game.getFildInfo(user).forEach(f -> ret.add(new FieldResponse(f)));
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
    public DeferredResult<HttpStatus> isMyTurn() {
        Long timeOutInMilliSec = 100000L;
        User user = userService.currentUser().get();
        DeferredResult<HttpStatus> deferredResult = new DeferredResult<>(timeOutInMilliSec, HttpStatus.BAD_REQUEST);
        CompletableFuture.runAsync(() -> {
            try {
                //Long pooling task;If task is not completed within 100 sec timeout response return for this request
                if(this.isMyTurn(userRepository, user.getId())) {
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
        if(user.finishTurn()) {
            log.info("Next Player");
            User next = game.nextPlayer();
            next.next();
            userRepository.save(user);
            userRepository.save(next);
           // meepleRepository.saveAll(test.getMeepleList());
        } else {
            throw new NotYourTurnException();
        }
    }

    @PutMapping("/{meepleId}/{x}/{y}")
    public void move(@PathVariable int meepleId, @PathVariable int x, @PathVariable int y) throws CannotMoveException, HasAlreadyMovedException, SpaceStationCannotMoveException {
        Optional<AbstractMeeple> meeple = meepleRepository.findById(meepleId);
        System.out.println(meepleId + " sould move frome "+ meeple.get().getField().getCoordinate() +" to: " + x + "/" + y);
        Optional<User> loggedIn = userService.currentUser();
        Map map = mapRepository.getTheMap();
        if (loggedIn.isPresent() && meeple.isPresent()) {
            if (meeple.get().move(map, map.findCoordinate(x, y))) {
            } else {
                throw new CannotMoveException();
            }
            meepleRepository.save(meeple.get());
            mapRepository.save(map);

        }

    }
}
