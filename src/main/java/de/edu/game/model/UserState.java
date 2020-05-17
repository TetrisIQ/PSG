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


    public void nextState() {
        switch (this.state) {
            case REGISTER:
                this.state = UserStates.READY;
                break;
            case READY:
                this.state = UserStates.WAITING;
                break;
            case WAITING:
                this.state = UserStates.MYTURN;
                break;
            default:
                this.state = UserStates.INVALID;
        }
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
