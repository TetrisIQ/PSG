package de.edu.game.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Model Class representing the state of the {@link User}
 */
@Entity
public class UserState {


    public UserState() {
    }

    /**
     * States of the {@link User}
     */
    private enum UserStates {
        REGISTER, READY, MYTURN, WAITING, INVALID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;

    private UserStates state = UserStates.REGISTER;


    /**
     * Switch to next state of the user
     * @return True if the change was successful
     */
    public boolean nextState() {
        switch (this.state) {
            case REGISTER:
                this.state = UserStates.READY;
                return true;
            case READY:
            case WAITING:
                this.state = UserStates.MYTURN;
                return true;
            case MYTURN:
                this.state = UserStates.WAITING;
                return true;
            default:
                this.state = UserStates.INVALID;
                return false;
        }
    }

    /**
     * Check if the user is in state MYTURN
     * @return True if the user in on turn
     */
    public boolean isMyturn() {
        return this.state.equals(UserStates.MYTURN);
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
