package com.klaha.renthub.exception;

public class AlreadyConfirmedException extends RuntimeException {
    public AlreadyConfirmedException(String message) {
        super(message);
    }
}
