package de.edu.game;

import de.edu.game.model.Game;
import de.edu.game.model.User;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.UserRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Class witch will be called on startup of Spring Boot
 */
@Component
public class StartupRunner implements CommandLineRunner {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final String PASSWORD = generateString();
    private static final String USERNAME = "admin";
    public static final User ADMIN = new User(0, "root", "none", encoder.encode(PASSWORD), USERNAME);
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(StartupRunner.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     * Creates an Admin {@link User} and the default {@link Game} <br>
     *
     * @param args Program arguments
     */
    @Override
    public void run(String... args) {
        log.info("Root user     \t=> \t {}", ADMIN.getUsername());
        log.info("Root password \t=> \t {}", PASSWORD);
        userRepository.save(ADMIN);

        // Create Default Game
        gameRepository.save(new Game());
        log.info("Default game Created, with id: {}", gameRepository.getTheGame().toString());

        // a small tool, this sends a notification if the server is ready, but only on Linux computers
        try {
            String[] cmd = {"/usr/bin/notify-send",
                    "Server Ready"};
            Runtime.getRuntime().exec(cmd);
        } catch (Exception ex) {
            //ignore, if the notification will not send its no big deal
        }


    }


    /**
     * Generate a random passwords, using the UUID Class
     *
     * @return a Random Password as String
     */
    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
