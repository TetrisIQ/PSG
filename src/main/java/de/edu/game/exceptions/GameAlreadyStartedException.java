package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GameAlreadyStartedException extends Exception {

    public GameAlreadyStartedException() {
        super("The Game has already started, you cannot join/start a running Game");
    }
}
