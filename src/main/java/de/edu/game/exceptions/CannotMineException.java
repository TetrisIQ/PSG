package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CannotMineException extends Exception {

    public CannotMineException() {
        super("You cannot mine here. The Asteroid has no energy to mine");
    }
}
