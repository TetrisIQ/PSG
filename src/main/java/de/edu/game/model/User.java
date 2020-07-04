package de.edu.game.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Model class for a User
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Log4j2
public class User {

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
}
