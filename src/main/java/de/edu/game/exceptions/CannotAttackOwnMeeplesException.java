package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CannotAttackOwnMeeplesException extends Exception {

    public CannotAttackOwnMeeplesException() {
        super("You cannot attack your own meeples");
    }
}
