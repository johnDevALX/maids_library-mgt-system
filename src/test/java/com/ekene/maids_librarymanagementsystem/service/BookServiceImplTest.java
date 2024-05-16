package com.ekene.maids_librarymanagementsystem.service;

import com.ekene.maids_librarymanagementsystem.book.dto.BookDto;
import com.ekene.maids_librarymanagementsystem.book.model.Author;
import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.book.repository.AuthorRepository;
import com.ekene.maids_librarymanagementsystem.book.repository.BookRepository;
import com.ekene.maids_librarymanagementsystem.book.service.BookServiceImpl;
import com.ekene.maids_librarymanagementsystem.cache.SystemCache;
import com.ekene.maids_librarymanagementsystem.utils.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private BookServiceImpl bookService;

    @Mock
    private SystemCache systemCache;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository, authorRepository, systemCache);
    }

    @Test
    void testSaveBook() {
        // Arrange
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Book Title");
        bookDto.setAuthorEmail(Arrays.asList("author1@example.com", "author2@example.com"));

        Author author1 = new Author();
        author1.setEmail("author1@example.com");
        Author author2 = new Author();
        author2.setEmail("author2@example.com");

        List<Author> authors = Arrays.asList(author1, author2);
        Book book = new Book();
        book.setAuthors(authors);
        // Set properties of the book as needed

        when(authorRepository.findAllByEmailIgnoreCase(bookDto.getAuthorEmail())).thenReturn(authors);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        BookDto savedBookDto = bookService.saveBook(bookDto);

        // Assert
        assertNotNull(savedBookDto);
        verify(authorRepository, times(1)).findAllByEmailIgnoreCase(bookDto.getAuthorEmail());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        // Arrange
        String isbn = "978-0123456789";
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Updated Book Title");
        bookDto.setAuthorEmail(Arrays.asList("author1@example.com", "author2@example.com"));
        Author author1 = new Author();
        Author author2 = new Author();
        List<Author> authors = Arrays.asList(author1, author2);
        Book existingBook = new Book();
        Book updatedBook = new Book();
        updatedBook.setAuthors(authors);


        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findAllByEmailIgnoreCase(bookDto.getAuthorEmail())).thenReturn(authors);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // Act
        BookDto updatedBookDto = bookService.updateBook(isbn, bookDto);

        // Assert
        assertNotNull(updatedBookDto);
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
        verify(authorRepository, times(1)).findAllByEmailIgnoreCase(bookDto.getAuthorEmail());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testGetBook() {
        // Arrange
        String isbn = "978-0123456789";
        Book book = new Book();
        book.setAuthors(Collections.emptyList());
//        book.setAuthors(null); // Set authors to null to simulate the issue

        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));

        // Act
        BookDto bookDto = bookService.getBook(isbn);

        // Assert
        assertNotNull(bookDto);
        verify(bookRepository, times(1)).findBookByIsbn(isbn);

        assertEquals(Collections.emptyList(), bookDto.getAuthorEmail());
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        int page = 0;
        int size = 10;

        String isbn = "978-0123456789";
        Book book1 = new Book();
        book1.setIsbn(isbn);
        book1.setAuthors(Collections.emptyList());

        String isbn2 = "978-0123456789";
        Book book2 = new Book();
        book2.setIsbn(isbn2);
        book2.setAuthors(Collections.emptyList());

        PageRequest pageRequest = PageRequest.of(page, size);
        List<Book> books = Arrays.asList(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books);

        when(bookRepository.findAll(pageRequest)).thenReturn(bookPage);

        // Act
        Page<BookDto> bookDtoPage = bookService.getAllBooks(page, size);

        // Assert
        assertNotNull(bookDtoPage);
        assertEquals(2, bookDtoPage.getContent().size());
        verify(bookRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        String isbn = "978-0123456789";
        Book book = new Book();

        when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));

        // Act
        String result = bookService.deleteBook(isbn);

        // Assert
        assertEquals("Book successfully deleted!", result);
        verify(bookRepository, times(1)).findBookByIsbn(isbn);
        verify(bookRepository, times(1)).delete(book);
    }
}