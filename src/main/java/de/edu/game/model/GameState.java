package de.edu.game.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Model Class representing the state of the {@link Game}
 */
@Entity
public class GameState {

    /**
     * States of the {@link Game}
     */
    private enum GameStates {
        LOBBY, PLAYING, FINISHED, INVALID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private GameStates state;

    public GameState() {
        this.state = GameStates.LOBBY;
    }

    /**
     * Change the State of the Game to Started
     *
     * @return True if the state changed successfully
     */
    public boolean startGame() {
        if (this.state.equals(GameStates.LOBBY)) {
            this.nextState();
            return true;
        }
        return false;
    }

    /**
     * Checks if the game has Started
     *
     * @return True if the game has Started
     */
    public boolean isGameStarted() {
        return this.state.equals(GameStates.PLAYING);
    }

    /**
     * Switch to the next state of the game
     */
    public void nextState() {
        switch (this.state) {
            case LOBBY:
                this.state = GameStates.PLAYING;
                break;
            case PLAYING:
                this.state = GameStates.FINISHED;
                break;
            default:
                this.state = GameStates.INVALID;
        }
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
