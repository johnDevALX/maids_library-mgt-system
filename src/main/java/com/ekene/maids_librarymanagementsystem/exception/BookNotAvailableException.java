package com.ekene.maids_librarymanagementsystem.exception;

public class BookNotAvailableException extends RuntimeException{

    public BookNotAvailableException(String message) {
        super(message);
    }

    public BookNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
