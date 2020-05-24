package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class HasAlreadyMovedException extends Exception {
    public HasAlreadyMovedException() {
        super("The Meeple has already moved in this round. You can move a Meeple only once per Round.");
    }
}
