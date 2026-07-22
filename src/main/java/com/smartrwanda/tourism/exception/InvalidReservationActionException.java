package com.smartrwanda.tourism.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReservationActionException extends RuntimeException {
    public InvalidReservationActionException(String message) {
        super(message);
    }
}
