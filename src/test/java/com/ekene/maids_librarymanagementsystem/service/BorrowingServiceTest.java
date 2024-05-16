package com.ekene.maids_librarymanagementsystem.service;

import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.book.repository.BookRepository;
import com.ekene.maids_librarymanagementsystem.exception.BookNotAvailableException;
import com.ekene.maids_librarymanagementsystem.exception.BookNotFound;
import com.ekene.maids_librarymanagementsystem.patron.model.MembershipType;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.patron.repository.PatronRepository;
import com.ekene.maids_librarymanagementsystem.record.model.BorrowingRecord;
import com.ekene.maids_librarymanagementsystem.record.repository.BorrowingRepository;
import com.ekene.maids_librarymanagementsystem.record.service.BorrowingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BorrowingServiceTest {

    private BorrowingService borrowingService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        borrowingService = new BorrowingService(bookRepository, patronRepository, borrowingRepository);
    }

    @Test
    void testBorrowBook_BookAvailable_PatronBelowLimit() {
        Long bookId = 978L;
        Long patronId = 9L;
        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);
        book.setInventory(2);
        Patron patron = new Patron();
        patron.setId(patronId);
        patron.setBorrowedBooks(0);
        patron.setMembershipType(MembershipType.REGULAR);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BorrowingRecord borrowingRecord = borrowingService.borrowBook(bookId, patronId);

        assertNotNull(borrowingRecord);
        assertEquals(book, borrowingRecord.getBook());
        assertEquals(patron, borrowingRecord.getPatron());
        assertEquals(LocalDate.now(), borrowingRecord.getBorrowedDate());
        assertTrue(book.getAvailable());
        assertEquals(1, book.getInventory());
        assertEquals(1, patron.getBorrowedBooks());
        verify(bookRepository, times(1)).save(book);
        verify(patronRepository, times(1)).save(patron);
        verify(borrowingRepository, times(1)).save(borrowingRecord);
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        Long bookId = 978L;
        Long patronId = 9L;
        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(false);
        book.setInventory(1);
        Patron patron = new Patron();
        patron.setId(patronId);
        patron.setBorrowedBooks(0);
        patron.setMembershipType(MembershipType.REGULAR);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

        assertThrows(BookNotAvailableException.class, () -> borrowingService.borrowBook(bookId, patronId));
        verify(bookRepository, times(1)).findById(bookId);
        verify(patronRepository, times(1)).findById(patronId);
        verify(bookRepository, never()).save(any(Book.class));
        verify(patronRepository, never()).save(any(Patron.class));
        verify(borrowingRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_ValidReturn() {
        Long bookId = 978L;
        Long patronId = 9L;
        LocalDate returnDate = LocalDate.now();
        Book book = new Book();
        book.setAvailable(false);
        book.setInventory(0);
        Patron patron = new Patron();
        patron.setId(patronId);
        patron.setBorrowedBooks(1);
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRepository.findByBookAndPatron(book, patron)).thenReturn(Optional.of(borrowingRecord));
        when(borrowingRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BorrowingRecord returnedBorrowingRecord = borrowingService.returnBook(bookId, patronId, returnDate);

        assertNotNull(returnedBorrowingRecord);
        assertEquals(book, returnedBorrowingRecord.getBook());
        assertEquals(patron, returnedBorrowingRecord.getPatron());
        assertEquals(returnDate, returnedBorrowingRecord.getReturnedDate());
        assertTrue(book.getAvailable());
        assertEquals(1, book.getInventory());
        assertEquals(0, patron.getBorrowedBooks());
        verify(bookRepository, times(1)).save(book);
        verify(patronRepository, times(1)).save(patron);
        verify(borrowingRepository, times(1)).findByBookAndPatron(book, patron);
        verify(borrowingRepository, times(1)).save(borrowingRecord);
    }

    @Test
    void testReturnBook_BookNotFound() {
        Long bookId = 978L;
        Long patronId = 9L;
        LocalDate returnDate = LocalDate.now();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFound.class, () -> borrowingService.returnBook(bookId, patronId, returnDate));
        verify(bookRepository, times(1)).findById(bookId);
        verify(patronRepository, never()).findById(anyLong());
        verify(borrowingRepository, never()).findByBookAndPatron(any(Book.class), any(Patron.class));
        verify(bookRepository, never()).save(any(Book.class));
        verify(patronRepository, never()).save(any(Patron.class));
        verify(borrowingRepository, never()).save(any(BorrowingRecord.class));
    }
}
