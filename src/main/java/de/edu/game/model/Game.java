package de.edu.game.model;

import de.edu.game.config.loader.ConfigLoader;
import lombok.Getter;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Log
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
    public boolean startGame() {
        return this.state.startGame() ? setupGame() : false;
    }

    private boolean setupGame() {
        this.map = new Map(ConfigLoader.shared.getRows(), ConfigLoader.shared.getColumns());
        this.spawnSpaceStations();
        Collections.shuffle(users);
        this.currentPlayer().next();
        log.info(this.currentPlayer().getUsername() + " starts the Game");
        return true;
    }

    private User currentPlayer() {
        return this.users.get(this.userTurnIndex);
    }

    public User nextPlayer() {
        userTurnIndex++;
        try {
            return currentPlayer();
        } catch (Exception ex) {
            //last player in list
            //Round finished
            this.userTurnIndex = 0;
            return currentPlayer();
        }
    }

    private void spawnSpaceStations() {
        int i = 0;
        for (User user : users) {
            // Spawn users Space Station
            Field field = this.map.findCoordinate(ConfigLoader.shared.getSpaceStations().get(i).getXCoordinate(), ConfigLoader.shared.getSpaceStations().get(i).getYCoordinate());
            SpaceStation spaceStation = new SpaceStation(this.map, user.getUsername(), field, "SpaceStation", user.getColor());
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
            returnList.add(meeple.getField(this.map));
        }
        return new HashSet<>(returnList);
    }

    public boolean isGameStarted() {
        return state.isGameStarted();
    }


    @Override //TODO: Improve toString for the StartupRunner
    public String toString() {
        return this.id + "";
    }


}
