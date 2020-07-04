package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import de.edu.game.repositorys.GameRepository;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Model Class witch represents the most game logic
 */
@Entity
@Getter
@Log4j2
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long timeoutRound = ConfigLoader.shared.getTimeoutInRounds();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GameState state;

    private long timeAfterRound = ConfigLoader.shared.getTimeAfterRound();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> users = new LinkedList<>();

    private int userTurnIndex = 0;

    @OneToOne(fetch = FetchType.LAZY)
    private Map map;

    private long threadId = -1;

    private int roundCounter = 1;

    private int maxRounds = ConfigLoader.shared.getMaxRounds();

    public Game() {
        this.state = new GameState();
    }


    /**
     * Registers a user at the Game
     *
     * @param user The user witch will register
     * @return True if the user successfully registered <br>
     * False if the user is not registered
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
     * @param gameRepository The {@link GameRepository} for calling nextPlayer
     * @return True if the game starts successfully <br> False if the game not started
     */
    public boolean startGame(GameRepository gameRepository) {
        return this.state.startGame() ? setupGame(gameRepository) : false;
    }

    /**
     * This setup the Game: create an map and Spawn the {@link SpaceStation}s
     *
     * @param gameRepository A {@link GameRepository} for selecting the first player.
     * @return True fif the game starts successful
     */
    private boolean setupGame(GameRepository gameRepository) {
        this.map = new Map(ConfigLoader.shared.getRows(), ConfigLoader.shared.getColumns());
        this.spawnSpaceStations();
        Collections.shuffle(users);
        User user = this.nextPlayer(gameRepository);
        user.next();
        this.map.spawnAsteroids(1);
        log.info("{} starts the Game", this.currentPlayer().getUsername());
        return true;
    }

    /**
     * return the player witch is currently on turn
     *
     * @return
     */
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

    /**
     * Schedule the next player event
     *
     * @param gameRepository {@link GameRepository} to get updated information later
     */
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

    /**
     * Managed the next players turn, and count rounds
     *
     * @param gameRepository {@link GameRepository} for scheduled next player
     * @return the User witch new turn is
     */
    public User nextPlayer(GameRepository gameRepository) {
        log.info("Next Player");
        userTurnIndex++;
        //schedule(userRepository);
        try {
            return currentPlayer();
        } catch (Exception ex) {
            //last player in list
            //Round finished
            this.roundCounter++; // increase roundCounter
            if (this.maxRounds - this.roundCounter < 10) {
                if (this.roundCounter <= 0) { // calculate winner
                    calculateWinner();
                }
                MapViewerMessageUpdate.send("Only " + (this.maxRounds - this.roundCounter) + " rounds Left!"); // adding message only some rounds left
            }
            this.userTurnIndex = 0;

            return currentPlayer();
        }
    }

    /**
     * Calculate witch player has the most VictoryPoints
     */
    private void calculateWinner() {
        User winner = users.get(0);
        for (User user : users) {
            if (user.getVictoryPoints() > winner.getVictoryPoints()) {
                winner = user;
            }
        }
        this.users.clear();
        this.users.add(winner);
    }

    /**
     * Sleep 10 seconds and then call nextPlayer, if the sleep will not interrupt
     *
     * @param gameRepository The {@link GameRepository} for calling nextPlayer
     */
    private void timeout(GameRepository gameRepository) {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nextPlayer(gameRepository);

    }

    /**
     * Spawn a {@link SpaceStation} and one {@link Starfighter} and two {@link Transporter} for every user.
     */
    private void spawnSpaceStations() {
        int i = 0;
        for (User user : users) {
            // Spawn users Space Station
            Field field = this.map.findCoordinate(ConfigLoader.shared.getSpaceStations().get(i).getXCoordinate(), ConfigLoader.shared.getSpaceStations().get(i).getYCoordinate());
            SpaceStation spaceStation = new SpaceStation(user, field, user.getColor());
            user.setSpaceStation(spaceStation);
            field.setMeeple(spaceStation);
            spaceStation.spawnStarfighter(map);
            spaceStation.spawnTransporter(map);
            spaceStation.spawnTransporter(map);
            i++;
        }
    }

    /**
     * @return True if the server is in State Ready <br>
     * False if the server is not in State Ready
     */
    public boolean isReady() {
        return state.toString().equals("Ready");
    }

    /**
     * Get information about all fields a player can see.
     *
     * @param user The @{@link User} how wants to see the Fields around his meeples
     * @return A set of {@link Field}s
     */
    public Set<Field> getFieldInfo(User user) {
        List<AbstractMeeple> ls = user.getMeepleList();
        List<Field> returnList = new LinkedList<>();
        ls.add(user.getSpaceStation());
        for (AbstractMeeple meeple : ls) {
            returnList.addAll(meeple.getFieldsAround(this.map));
            returnList.add(meeple.getField());
        }
        return new HashSet<>(returnList);
    }

    /**
     * Checks if the game has Started
     *
     * @return True if the game has started <br>
     * False if the game has not started
     */
    public boolean isGameStarted() {
        return state.isGameStarted();
    }


    @Override
    public String toString() {
        return Integer.toString(this.id);
    }


}
