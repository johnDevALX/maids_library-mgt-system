package com.ekene.maids_librarymanagementsystem.exception;

public class PatronNotFound extends RuntimeException{
    public PatronNotFound(String message) {
        super(message);
    }

    public PatronNotFound(String message, Throwable cause) {
        super(message, cause);
    }


    public PatronNotFound() {

    }
}
