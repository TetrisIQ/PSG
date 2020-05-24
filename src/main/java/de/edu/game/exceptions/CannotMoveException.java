package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CannotMoveException extends Exception {

    public CannotMoveException(){
        super("Cannot move, maybe the field you want to move is not empty. Transporter cannot move to a full field.");
    }
}
