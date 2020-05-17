package de.edu.game.controller;

import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import de.edu.game.config.UserService;
import de.edu.game.model.Game;
import de.edu.game.model.User;
import de.edu.game.repositorys.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameRepository gameRepository;


    @Autowired
    private UserService userService;

    @GetMapping
    public DeferredResult<String> getMapInformation() {
        Game game = gameRepository.getTheGame();
        Long timeOutInMilliSec = game.getTimeout();
        DeferredResult<String> deferredResult = new DeferredResult<>(timeOutInMilliSec,"Time Out");
        CompletableFuture.runAsync(()->{
            try {
                User user = userService.currentUser().get();
                while (true) {
                    if(user.myTurn()) {
                        deferredResult.setResult(game.getFildInfo(user));
                    }
                    //TimeUnit.SECONDS.sleep(100);
                }
            }catch (Exception ex){
            }
        });
        return deferredResult;
    }
}
