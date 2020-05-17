package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.IM_USED)
public class UsernameTakenException extends Exception {

    public UsernameTakenException(String msg) {
        super(msg);
    }

}
