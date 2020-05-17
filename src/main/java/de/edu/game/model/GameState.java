package de.edu.game.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
public class GameState {

    private enum GameStates {
        LOBBY, PLAYING, FINISHED, INVALID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;

    private GameStates state;

    public GameState(){
        this.state = GameStates.LOBBY;
    }

    public boolean startGame() {
        if (this.state.equals(GameStates.LOBBY)) {
            this.nextState();
            return true;
        }
        return false;
    }

    public boolean isGameStarted() {
        return this.state.equals(GameStates.PLAYING);
    }

    private void nextState() {
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
