package de.edu.game.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Log
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

    public boolean finishTurn() {
        if (this.myTurn()) {
            this.state.nextState();
            this.meepleList.forEach(m -> m.setHasMoved(false));
            return true;
        }
        return false;
    }

    public boolean playerReady(User user) {
        return false;
    }


    public boolean myTurn() {
        return state.isMyturn();
    }

    public boolean addMeeple(AbstractMeeple meeple) {
        if (meeple.getName().equals("SpaceStation")) {
            log.info("SpaceStations should not be in the meeple list. Use the property spaceStation instead!");
            return false;
        }
        return this.meepleList.add(meeple);
    }

    public void next() {
        this.state.nextState();
    }

    public void addPoints(int points) {
        this.victoryPoints += points;
    }
}
