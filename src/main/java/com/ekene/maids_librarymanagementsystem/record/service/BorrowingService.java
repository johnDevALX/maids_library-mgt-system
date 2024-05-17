package com.ekene.maids_librarymanagementsystem.record.service;

import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.book.repository.BookRepository;
import com.ekene.maids_librarymanagementsystem.exception.BookNotAvailableException;
import com.ekene.maids_librarymanagementsystem.exception.BookNotFound;
import com.ekene.maids_librarymanagementsystem.exception.BorrowingRecordNotFound;
import com.ekene.maids_librarymanagementsystem.exception.MultipleBorrowingException;
import com.ekene.maids_librarymanagementsystem.exception.PatronBorrowLimitExceededException;
import com.ekene.maids_librarymanagementsystem.exception.PatronNotFound;
import com.ekene.maids_librarymanagementsystem.patron.model.MembershipType;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.patron.repository.PatronRepository;
import com.ekene.maids_librarymanagementsystem.record.model.BorrowingRecord;
import com.ekene.maids_librarymanagementsystem.record.repository.BorrowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowingService {
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRepository borrowingRepository;

    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFound("Book not found"));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFound("Patron not found"));
        Optional<BorrowingRecord> saved = borrowingRepository.findAllByBookAndPatron(book, patron)
                .stream()
                .filter(br -> br.getReturnedDate() == null)
                .findFirst();

        if (saved.isPresent()){
            throw new MultipleBorrowingException("unreturned Previous borrowed book from patron");
        }
        boolean patronMax = patron.getBorrowedBooks() < patron.getMembershipType().getMaxBorrowedBooks();
        boolean inventoryCheck = book.getInventory() > 0;
        if (Boolean.TRUE.equals(book.getAvailable()) && patronMax && inventoryCheck) {
            BorrowingRecord borrowingRecord = new BorrowingRecord();
            borrowingRecord.setBook(book);
            borrowingRecord.setPatron(patron);
            borrowingRecord.setBorrowedDate(LocalDate.now());
            borrowingRecord.setDueDate(calculateDueDate(patron.getMembershipType()));

            patron.setBorrowedBooks(patron.getBorrowedBooks() + 1);
            book.setInventory(book.getInventory() - 1);
            book.setAvailable(book.getInventory() > 0);
            bookRepository.save(book);
            patronRepository.save(patron);
            return borrowingRepository.save(borrowingRecord);
        } else if (Boolean.FALSE.equals(book.getAvailable())) {
            throw new BookNotAvailableException("The book is not available for borrowing");
        } else if (Boolean.FALSE.equals(inventoryCheck)) {
            throw new BookNotAvailableException("The book is not available for borrowing {out of stock}");
        } else {
            throw new PatronBorrowLimitExceededException("The patron has reached the maximum number of allowed books for their membership type");
        }
    }

    public BorrowingRecord returnBook(Long bookId, Long patronId, LocalDate returnDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFound("Book not found"));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFound("Patron not found"));
        BorrowingRecord borrowingRecord = borrowingRepository.findAllByBookAndPatron(book, patron)
                .stream()
                .filter(br -> br.getReturnedDate() == null)
                .findFirst()
                .orElseThrow(() -> new BorrowingRecordNotFound("No active borrowing record found for the book and patron"));


        borrowingRecord.setReturnedDate(returnDate);
        book.setAvailable(true);
        patron.setBorrowedBooks(patron.getBorrowedBooks() - 1);
        book.setInventory(book.getInventory() + 1);
        bookRepository.save(book);
        patronRepository.save(patron);
        return borrowingRepository.save(borrowingRecord);
    }

    private LocalDate calculateDueDate(MembershipType membershipType) {
        return LocalDate.now().plusDays(membershipType.getLoanPeriodInDays());
    }
}