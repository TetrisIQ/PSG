package de.edu.game.controller;

import com.google.gson.Gson;
import de.edu.game.exceptions.GameAlreadyStartedException;
import de.edu.game.model.Game;
import de.edu.game.model.Map;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.MapRepository;
import de.edu.game.repositorys.MeepleRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:8080")
@Log
public class AdminController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private MeepleRepository meepleRepository;

    @PutMapping("/start")
    @ResponseStatus(code = HttpStatus.OK)
    //@PreAuthorize("hasRole('root')")
    public void startGame() throws GameAlreadyStartedException {
        //TODO: Make Authorization only for Root/Admin account.
        Game game = gameRepository.getTheGame();
        if (game.startGame()) {
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
    public String getMap() {
        //TODO: configure Spring to use the builtin JSON Parsing
        //Currently there are some issues,
        //There rows are empty
        //This should be a Temporary workaround
        try {
            Map map = mapRepository.getTheMap();
            return new Gson().toJson(map);
        } catch (IndexOutOfBoundsException ex) {
            return "Game not Started";
        }
    }

}




