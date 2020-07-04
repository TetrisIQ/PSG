package de.edu.game.model;

import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Model class for a User
 */
@Entity
public class User {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(User.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String role;

    private String color;

    private String password;

    private String username;

    private int victoryPoints = 0;

    @OneToOne(cascade = CascadeType.ALL)
    private SpaceStation spaceStation;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<AbstractMeeple> meepleList = new LinkedList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private UserState state = new UserState();

    public User(int id, String role, String color, String password, String username) {
        this.id = id;
        this.role = role;
        this.color = color;
        this.password = password;
        this.username = username;
    }

    public User() {
    }

    /**
     * Finish tuns for the user
     *
     * @return True if the user has finished his turn
     */
    public boolean finishTurn() {
        if (this.myTurn()) {
            this.state.nextState();
            this.meepleList.forEach(m -> m.setHasMoved(false));
            return true;
        }
        return false;
    }

    /**
     * Return True if the State of the User is myTurn
     *
     * @return True if the user is on Turn
     */
    public boolean myTurn() {
        return state.isMyturn();
    }


    /**
     * Add a meeple to the users meeple List
     *
     * @param meeple The {@link AbstractMeeple} to add
     */
    public void addMeeple(AbstractMeeple meeple) {
        if (meeple.getName().equals("SpaceStation")) {
            log.warn("SpaceStations should not be in the meeple list. Use the property spaceStation instead!");
            return;
        }
        this.meepleList.add(meeple);
    }

    /**
     * Next state of the user
     */
    public void next() {
        this.state.nextState();
    }

    /**
     * add Victory points to the user
     *
     * @param points victoryPoints to add
     */
    public void addPoints(int points) {
        this.victoryPoints += points;
    }

    public int getId() {
        return this.id;
    }

    public String getRole() {
        return this.role;
    }

    public String getColor() {
        return this.color;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    public SpaceStation getSpaceStation() {
        return this.spaceStation;
    }

    public List<AbstractMeeple> getMeepleList() {
        return this.meepleList;
    }

    public UserState getState() {
        return this.state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public void setSpaceStation(SpaceStation spaceStation) {
        this.spaceStation = spaceStation;
    }

    public void setMeepleList(List<AbstractMeeple> meepleList) {
        this.meepleList = meepleList;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", role=" + this.getRole() + ", color=" + this.getColor() + ", password=" + this.getPassword() + ", username=" + this.getUsername() + ", victoryPoints=" + this.getVictoryPoints() + ", spaceStation=" + this.getSpaceStation() + ", meepleList=" + this.getMeepleList() + ", state=" + this.getState() + ")";
    }
}
