package de.edu.game;

import de.edu.game.model.Game;
import de.edu.game.model.User;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //Same Root user for testing
        String pw = "123";
        String user = "admin";
        //Random Root User
        //String pw = generateString();
        //String user = generateString();
        User root = new User(0, "root", "none", encoder.encode(pw), user);
        log.info("Root user     \t=> \t" + root.getUsername());
        log.info("Root password \t=> \t" + pw);
        userRepository.save(root);

        // Create Default Game
        gameRepository.save(new Game());
        log.info("Default game Created, with id: "+ gameRepository.getTheGame().toString());
    }


    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
