package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotYourTurnException extends Exception {
    public NotYourTurnException() {
        super("It was not your turn.");
    }
}
