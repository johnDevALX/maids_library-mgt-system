package com.ekene.maids_librarymanagementsystem.exception;

public class BorrowingRecordNotFound extends RuntimeException {
    public BorrowingRecordNotFound(String message) {
        super(message);
    }

    public BorrowingRecordNotFound(String message, Throwable cause) {
        super(message, cause);
    }


    public BorrowingRecordNotFound() {}
}
