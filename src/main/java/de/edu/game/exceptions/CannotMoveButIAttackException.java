package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CannotMoveButIAttackException extends Exception {

    public CannotMoveButIAttackException(){
        super("Cannot move, the Enemy is still there, we must attack again.");
    }
}
