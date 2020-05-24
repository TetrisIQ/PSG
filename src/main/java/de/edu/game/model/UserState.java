package de.edu.game.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class UserState {

    private enum UserStates {
        REGISTER, READY, MYTURN, WAITING, INVALID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;

    private UserStates state = UserStates.REGISTER;


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

    boolean isMyturn() {
        return this.state.equals(UserStates.MYTURN);
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
