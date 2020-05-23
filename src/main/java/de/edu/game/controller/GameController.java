package de.edu.game.controller;

import de.edu.game.config.UserService;
import de.edu.game.controller.responses.FieldResponse;
import de.edu.game.model.*;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.MapRepository;
import de.edu.game.repositorys.MeepleRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
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
    private GameRepository gameRepository;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    MeepleRepository meepleRepository;

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

    private boolean isMyTurn(User user) {
        while (!user.myTurn()) {

        }
        if(user.myTurn()){
            return true;
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
                //Long pooling task;If task is not completed within 100 sec timeout response retrun for this request
                if(this.isMyTurn(user)) {
                    deferredResult.setResult(HttpStatus.OK);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return deferredResult;
    }

    @PutMapping("{meepleId}/{x}/{y}")
    public void Move(@PathVariable int meepleId, @PathVariable int x, @PathVariable int y) throws Exception {
        System.out.println(meepleId + " sould move to:" + x + "/" + y);
        Optional<AbstractMeeple> meeple = meepleRepository.findById(meepleId);
        Optional<User> loggedIn = userService.currentUser();
        Map map = mapRepository.getTheMap();
        if (loggedIn.isPresent() && meeple.isPresent()) {
            if (meeple.get().move(map.findCoordinate(x, y))) {
                System.out.println("Moved");
            } else {
                throw new Exception("Illegal move");
            }
            meepleRepository.save(meeple.get());
            mapRepository.save(map);

        }

    }
}
