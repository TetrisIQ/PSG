package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.repositorys.GameRepository;
import de.edu.game.repositorys.UserRepository;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Entity
@Getter
@Log4j2
public class Game {

    // ############### Parameter ###############
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long timeoutRound = ConfigLoader.shared.getTimeoutInRounds();

    @OneToOne(cascade = CascadeType.ALL)
    private GameState state;

    private long timeAfterRound = ConfigLoader.shared.getTimeAfterRound();

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> users = new LinkedList<>();

    private int userTurnIndex = 0;

    @OneToOne
    private Map map;

    private long threadId = -1;

    private int roundCounter = 1;

    private int maxRounds = ConfigLoader.shared.getMaxRounds();

    // ############### Constructor ###############
    public Game() {
        this.state = new GameState();
    }

    // ############### Methods ###############

    /**
     * Registers a user at the Game
     *
     * @param user The user witch will register
     * @return @true if the user successfully registered <br>@false if the user is not registered
     */
    public boolean registerUser(User user) {
        if (!state.isGameStarted()) {
            return this.users.add(user);
        }
        return false;
    }

    /**
     * Starts the Game
     *
     * @return @true if the game starts successfully <br>@false if the game not started
     */
    public boolean startGame(GameRepository gameRepository) {
        return this.state.startGame() ? setupGame(gameRepository) : false;
    }

    private boolean setupGame(GameRepository gameRepository) {
        this.map = new Map(ConfigLoader.shared.getRows(), ConfigLoader.shared.getColumns());
        this.spawnSpaceStations();
        Collections.shuffle(users);
        User user = this.nextPlayer(gameRepository);
        user.next();
        this.map.spawnAsteroids(1);
        log.info("{} starts the Game",this.currentPlayer().getUsername());
        return true;
    }

    private User currentPlayer() {
        return this.users.get(this.userTurnIndex);
    }

    private Thread getThreadById(long id) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    @SneakyThrows
    private void schedule(GameRepository gameRepository) {
        Thread thread;
        if (this.threadId == -1) { // First Thread started
            thread = new Thread(() -> timeout(gameRepository));
            this.threadId = thread.getId();
            gameRepository.save(this);
            thread.start();
        } else {
            getThreadById(this.threadId).interrupt();
            this.threadId = -1;
            schedule(gameRepository);
        }

    }

    public User nextPlayer(GameRepository gameRepository) {
        log.info("Next Player");
        userTurnIndex++;
        //schedule(userRepository);
        try {
            return currentPlayer();
        } catch (Exception ex) {
            //last player in list
            //Round finished
            this.roundCounter++;
            if(this.maxRounds - this.roundCounter < 10 ) {
                MapViewerMessageUpdate.send("Only " + (this.maxRounds - this.roundCounter) + " rounds Left!");
            }
            this.userTurnIndex = 0;

            return currentPlayer();
        }
    }

    private void timeout(GameRepository gameRepository) {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nextPlayer(gameRepository);

    }

    private void spawnSpaceStations() {
        int i = 0;
        for (User user : users) {
            // Spawn users Space Station
            Field field = this.map.findCoordinate(ConfigLoader.shared.getSpaceStations().get(i).getXCoordinate(), ConfigLoader.shared.getSpaceStations().get(i).getYCoordinate());
            SpaceStation spaceStation = new SpaceStation(this.map, user.getUsername(), field, user.getColor());
            user.setSpaceStation(spaceStation);
            field.setMeeple(spaceStation);
            spaceStation.spawnStarfighter(map, user);
            spaceStation.spawnTransporter(map, user);
            spaceStation.spawnTransporter(map, user);
            i++;
        }
    }


    // ############### Getter/Setter/etc. ###############
    public Long getTimeout() {
        return timeoutRound;
    }

    public boolean isReady() {
        return state.toString().equals("Ready");
    }

    public Set<Field> getFildInfo(User user) {
        List<AbstractMeeple> ls = user.getMeepleList();
        List<Field> returnList = new LinkedList<>();
        ls.add(user.getSpaceStation());
        for (AbstractMeeple meeple : ls) {
            returnList.addAll(meeple.getFieldsAround(this.map, this.map.findCoordinate(meeple.getField().getCoordinate().getXCoordinate(), meeple.getField().getCoordinate().getYCoordinate())));
            returnList.add(meeple.getField());
        }
        return new HashSet<>(returnList);
    }

    public boolean isGameStarted() {
        return state.isGameStarted();
    }


    @Override //TODO: Improve toString for the StartupRunner
    public String toString() {
        return Integer.toString(this.id);
    }


}
