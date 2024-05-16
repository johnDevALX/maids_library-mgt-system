package com.ekene.maids_librarymanagementsystem.patron.model;

public enum MembershipType {
    REGULAR(2, 15),
    STUDENT(4, 30),
    FACULTY(5, 30);

    private final int maxBorrowedBooks;
    private final int loanPeriodInDays;

    MembershipType(int maxBorrowedBooks, int loanPeriodInDays) {
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.loanPeriodInDays = loanPeriodInDays;
    }

    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    public int getLoanPeriodInDays() {
        return loanPeriodInDays;
    }
}
