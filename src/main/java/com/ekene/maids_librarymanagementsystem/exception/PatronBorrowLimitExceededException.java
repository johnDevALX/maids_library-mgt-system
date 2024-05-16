package com.ekene.maids_librarymanagementsystem.exception;

public class PatronBorrowLimitExceededException extends RuntimeException {
    public PatronBorrowLimitExceededException(String message) {
        super(message);
    }
}
