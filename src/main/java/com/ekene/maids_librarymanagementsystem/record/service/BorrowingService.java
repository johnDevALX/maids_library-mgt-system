package com.ekene.maids_librarymanagementsystem.record.service;

import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.book.repository.BookRepository;
import com.ekene.maids_librarymanagementsystem.exception.BookNotAvailableException;
import com.ekene.maids_librarymanagementsystem.exception.BookNotFound;
import com.ekene.maids_librarymanagementsystem.exception.PatronBorrowLimitExceededException;
import com.ekene.maids_librarymanagementsystem.patron.model.MembershipType;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.patron.repository.PatronRepository;
import com.ekene.maids_librarymanagementsystem.record.model.BorrowingRecord;
import com.ekene.maids_librarymanagementsystem.record.repository.BorrowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowingService {
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRepository borrowingRepository;

    public BorrowingRecord borrowBook(String isbn, String email) {
        Book book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new BookNotFound("Book not found"));
        Patron patron = patronRepository.findPatronByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Patron not found"));

        if (book.getAvailable() && patron.getBorrowedBooks() < patron.getMembershipType().getMaxBorrowedBooks()) {
            BorrowingRecord borrowingRecord = new BorrowingRecord();
            borrowingRecord.setBook(book);
            borrowingRecord.setPatron(patron);
            borrowingRecord.setBorrowedDate(LocalDate.now());
            borrowingRecord.setDueDate(calculateDueDate(patron.getMembershipType()));

            book.setAvailable(false);
            patron.setBorrowedBooks(patron.getBorrowedBooks() + 1);
            bookRepository.save(book);
            patronRepository.save(patron);
            return borrowingRepository.save(borrowingRecord);
        } else if (!book.getAvailable()) {
            throw new BookNotAvailableException("The book is not available for borrowing");
        } else {
            throw new PatronBorrowLimitExceededException("The patron has reached the maximum number of allowed books for their membership type");
        }
    }

    public BorrowingRecord returnBook(String isbn, String email, LocalDate returnDate) {
        Book book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new BookNotFound("Book not found"));
        Patron patron = patronRepository.findPatronByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Patron not found"));
        BorrowingRecord borrowingRecord = borrowingRepository.findByBookAndPatron(book, patron)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        borrowingRecord.setReturnedDate(returnDate);
        book.setAvailable(true);
        patron.setBorrowedBooks(patron.getBorrowedBooks() - 1);
        bookRepository.save(book);
        patronRepository.save(patron);
        return borrowingRepository.save(borrowingRecord);
    }

    private LocalDate calculateDueDate(MembershipType membershipType) {
        return LocalDate.now().plusDays(membershipType.getLoanPeriodInDays());
    }
}