package com.ekene.maids_librarymanagementsystem.patron.model;

public enum MembershipType {
    REGULAR(2, 15),
    STUDENT(10, 30),
    FACULTY(15, 30);
    // ... other membership types

    private final int maxBorrowedBooks;
    private final int loanPeriodInDays;
//    private final double feeDiscount;

    MembershipType(int maxBorrowedBooks, int loanPeriodInDays) {
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.loanPeriodInDays = loanPeriodInDays;
    }

    // Getters for the properties
    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    public int getLoanPeriodInDays() {
        return loanPeriodInDays;
    }
//
//    public double getFeeDiscount() {
//        return feeDiscount;
//    }
}
