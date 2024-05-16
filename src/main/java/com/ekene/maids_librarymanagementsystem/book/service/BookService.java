package com.ekene.maids_librarymanagementsystem.book.service;

import com.ekene.maids_librarymanagementsystem.book.dto.BookDto;
import com.ekene.maids_librarymanagementsystem.book.model.Book;
import org.springframework.data.domain.Page;

public interface BookService {
    BookDto saveBook(BookDto bookDto);

    BookDto updateBook(String isbn, BookDto bookDto);

    BookDto getBook(String isbn);

    Page<BookDto> getAllBooks(int page, int size);

    String deleteBook(String isbn);
}
