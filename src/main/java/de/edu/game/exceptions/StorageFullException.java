package de.edu.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StorageFullException extends Exception {
    public StorageFullException() {
        super("The Storage is full. You cannot add more Energy to it.");
    }
}
