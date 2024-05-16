package com.ekene.maids_librarymanagementsystem.exception;

public class BookNotFound extends RuntimeException{
    public BookNotFound(String message) {
        super(message);
    }

    public BookNotFound(String message, Throwable cause) {
        super(message, cause);
    }


    public BookNotFound() {

    }
}
