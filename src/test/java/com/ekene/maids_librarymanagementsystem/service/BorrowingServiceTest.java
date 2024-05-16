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
        // Arrange
        String isbn = "978-0123456789";
        String email = "patron@example.com";
        Book book = new Book();
        book.setAvailable(true);
        Patron patron = new Patron();
        patron.setBorrowedBooks(0);
        patron.setMembershipType(MembershipType.REGULAR);

        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));
        when(patronRepository.findPatronByEmailIgnoreCase(email)).thenReturn(Optional.of(patron));
        when(borrowingRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BorrowingRecord borrowingRecord = borrowingService.borrowBook(isbn, email);

        // Assert
        assertNotNull(borrowingRecord);
        assertEquals(book, borrowingRecord.getBook());
        assertEquals(patron, borrowingRecord.getPatron());
        assertEquals(LocalDate.now(), borrowingRecord.getBorrowedDate());
//        assertEquals(LocalDate.now().plusDays(30), borrowingRecord.getDueDate());
        assertFalse(book.getAvailable());
        assertEquals(1, patron.getBorrowedBooks());
        verify(bookRepository, times(1)).save(book);
        verify(patronRepository, times(1)).save(patron);
        verify(borrowingRepository, times(1)).save(borrowingRecord);
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        // Arrange
        String isbn = "978-0123456789";
        String email = "patron@example.com";
        Book book = new Book();
        book.setAvailable(false);
        Patron patron = new Patron();

        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));
        when(patronRepository.findPatronByEmailIgnoreCase(email)).thenReturn(Optional.of(patron));

        // Act & Assert
        assertThrows(BookNotAvailableException.class, () -> borrowingService.borrowBook(isbn, email));
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
        verify(patronRepository, times(1)).findPatronByEmailIgnoreCase(email);
        verify(bookRepository, never()).save(any(Book.class));
        verify(patronRepository, never()).save(any(Patron.class));
        verify(borrowingRepository, never()).save(any(BorrowingRecord.class));
    }

    @Test
    void testReturnBook_ValidReturn() {
        // Arrange
        String isbn = "978-0123456789";
        String email = "patron@example.com";
        LocalDate returnDate = LocalDate.now();
        Book book = new Book();
        book.setAvailable(false);
        Patron patron = new Patron();
        patron.setBorrowedBooks(1);
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);

        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));
        when(patronRepository.findPatronByEmailIgnoreCase(email)).thenReturn(Optional.of(patron));
        when(borrowingRepository.findByBookAndPatron(book, patron)).thenReturn(Optional.of(borrowingRecord));
        when(borrowingRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BorrowingRecord returnedBorrowingRecord = borrowingService.returnBook(isbn, email, returnDate);

        // Assert
        assertNotNull(returnedBorrowingRecord);
        assertEquals(book, returnedBorrowingRecord.getBook());
        assertEquals(patron, returnedBorrowingRecord.getPatron());
        assertEquals(returnDate, returnedBorrowingRecord.getReturnedDate());
        assertTrue(book.getAvailable());
        assertEquals(0, patron.getBorrowedBooks());
        verify(bookRepository, times(1)).save(book);
        verify(patronRepository, times(1)).save(patron);
        verify(borrowingRepository, times(1)).findByBookAndPatron(book, patron);
        verify(borrowingRepository, times(1)).save(borrowingRecord);
    }

    @Test
    void testReturnBook_BookNotFound() {
        // Arrange
        String isbn = "978-0123456789";
        String email = "patron@example.com";
        LocalDate returnDate = LocalDate.now();

        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFound.class, () -> borrowingService.returnBook(isbn, email, returnDate));
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
        verify(patronRepository, never()).findPatronByEmailIgnoreCase(anyString());
        verify(borrowingRepository, never()).findByBookAndPatron(any(Book.class), any(Patron.class));
        verify(bookRepository, never()).save(any(Book.class));
        verify(patronRepository, never()).save(any(Patron.class));
        verify(borrowingRepository, never()).save(any(BorrowingRecord.class));
    }
}
