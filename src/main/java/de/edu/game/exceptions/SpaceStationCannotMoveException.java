package de.edu.game.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class SpaceStationCannotMoveException extends Exception {
    public SpaceStationCannotMoveException() {
        super("The spaceStation cannot move");
    }
}
