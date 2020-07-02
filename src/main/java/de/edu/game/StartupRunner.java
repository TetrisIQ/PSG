package de.edu.game;

import de.edu.game.model.Game;
import de.edu.game.model.User;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log4j2
public class StartupRunner implements CommandLineRunner {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final String pw = "123"; // TODO: use Random passowrd for admin account
    private static final String user = "admin";
    public static final User ADMIN = new User(0, "root", "none", encoder.encode(pw), user);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public void run(String... args) throws Exception {
        //Same Root user for testing
        String pw = "123"; // TODO: use Random passowrd for admin account
        String user = "admin";
        //Random Root User
        //String pw = generateString();

        log.info("Root user     \t=> \t {}", ADMIN.getUsername());
        log.info("Root password \t=> \t {}", pw);
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


    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        //return uuid.replace("-", "");
        //TODO: Testing is easier if we return the same password
        return "7b13984732384f60854f0fe451d01241";
    }
}
