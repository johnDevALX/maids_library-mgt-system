package com.ekene.maids_librarymanagementsystem.exception;

public class MultipleBorrowingException extends RuntimeException {
    public MultipleBorrowingException(String message) {
        super(message);
    }

    public MultipleBorrowingException(String message, Throwable cause) {
        super(message, cause);
    }


    public MultipleBorrowingException() {

    }
}
