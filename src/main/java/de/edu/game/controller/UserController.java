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
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for handling UserRegistration and first Login
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    ;

    /**
     * First register a new {@link User}
     *
     * @param username The username
     * @return An Response Object of the @{@link User} with an <b>random</b> password
     * @throws UsernameTakenException      If the username already exists
     * @throws GameAlreadyStartedException If the Game has already started
     */
    @PostMapping("/register/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Registration successful"),
            @ApiResponse(code = 226, message = "Invalid username")})
    public UserResponse register(@PathVariable String username) throws UsernameTakenException, GameAlreadyStartedException {
        if (gameRepository.getTheGame().isGameStarted()) {
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

    /**
     * First login of the User
     *
     * @return An Response Object of the @{@link User} without the password
     * @throws GameAlreadyStartedException If the Game has already started
     * @throws UserNotFoundException       If the user has not register first
     */
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

    /**
     * Add a user to the @{@link Game}
     *
     * @param user The User who will be added
     * @throws GameAlreadyStartedException If the Game has already started
     */
    private void addUserToGame(User user) throws GameAlreadyStartedException {
        Game game = gameRepository.getTheGame();
        if (game.registerUser(user)) {
            gameRepository.save(game);
            log.info("{} ({}) joined the Game", user.getUsername(), user.getId());
        } else {
            // game is started, cannot Join a running Game
            throw new GameAlreadyStartedException();
        }
    }
}
