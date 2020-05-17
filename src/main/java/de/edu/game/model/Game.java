package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import lombok.Getter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
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

    @OneToOne
    private Map map;

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
            boolean ret = this.users.add(user);
            return ret;
        }
        return false;
    }

    /**
     * Starts the Game
     *
     * @return @true if the game starts successfully <br>@false if the game not started
     */
    public boolean startGame() {
        return this.state.startGame() ? setupGame() : false;
    }

    private boolean setupGame() {
        this.map = new Map(ConfigLoader.shared.getRows(), ConfigLoader.shared.getColumns());
        this.spawnSpaceStations();


        return true;
    }

    private void spawnSpaceStations() {
        int i = 0;
        for (User user : users) {
            // Spawn users Space Station
            System.out.println(ConfigLoader.shared.getSpaceStations());
            Field field = this.map.findCoordinate(ConfigLoader.shared.getSpaceStations().get(i).getXCoordinate(), ConfigLoader.shared.getSpaceStations().get(i).getYCoordinate());
            SpaceStation spaceStation = new SpaceStation(this.map, user.getUsername(), field.getCoordinate(), "SpaceStation", user.getColor());
            user.setSpaceStation(spaceStation);
            field.setMeeple(spaceStation);
            spaceStation.spawnStarfighter(user);
            spaceStation.spawnTransporter(user);
            spaceStation.spawnTransporter(user);
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

    public String getFildInfo(User user) {
        return "";
    }

    public boolean isGameStarted() {
        return state.isGameStarted();
    }


    @Override //TODO: Improve toString for the StartupRunner
    public String toString() {
        return this.id + "";
    }
}
