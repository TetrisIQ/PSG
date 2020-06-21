package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotEnoughEnergyException extends Exception {
    public NotEnoughEnergyException() {
        super("You have not enough energy to do this.");
    }
}
