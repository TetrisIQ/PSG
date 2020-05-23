package de.edu.game.controller;

import de.edu.game.StartupRunner;
import de.edu.game.config.UserService;
import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.controller.responses.UserResponse;
import de.edu.game.exceptions.GameAlreadyStartedException;
import de.edu.game.exceptions.UserNotFoundException;
import de.edu.game.exceptions.UsernameTakenException;
import de.edu.game.model.Game;
import de.edu.game.model.User;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.UserRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@Log
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    ;

    @PostMapping("/register/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Registration successful"),
            @ApiResponse(code = 226, message = "Invalid username")})
    public UserResponse register(@PathVariable String username) throws UsernameTakenException, GameAlreadyStartedException {
        if(gameRepository.getTheGame().isGameStarted()) {
            throw new GameAlreadyStartedException();
        }
        String pw = StartupRunner.generateString();
        String color = ConfigLoader.shared.getRandomColor();
        if (!(userRepository.findByUsername(username).isPresent())) {
            User user = new User(0, "player", color, encoder.encode(pw), username);
            userRepository.save(user);
            return new UserResponse(username, pw, color, user.getState().toString());
        } else {
            throw new UsernameTakenException("Username taken");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public UserResponse login() throws GameAlreadyStartedException, UserNotFoundException {
        Optional<User> user = userService.currentUser();
        if (user.isPresent()) {
            user.get().getState().nextState();
            addUserToGame(user.get());
            return new UserResponse(user.get());
        }
        throw new UserNotFoundException("User Not found");
    }

    private void addUserToGame(User user) throws GameAlreadyStartedException {
        Game game = gameRepository.getTheGame();
        if (game.registerUser(user)) {
            gameRepository.save(game);
            log.info(user.getUsername() + " joined the Game");
        } else  {
            // game is started, cannot Join a running Game
            throw new GameAlreadyStartedException();
        }
    }
}
