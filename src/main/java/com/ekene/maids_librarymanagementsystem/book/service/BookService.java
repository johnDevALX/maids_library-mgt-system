package com.ekene.maids_librarymanagementsystem.book.service;

import com.ekene.maids_librarymanagementsystem.book.dto.BookDto;
import com.ekene.maids_librarymanagementsystem.book.model.Book;
import org.springframework.data.domain.Page;

public interface BookService {
    BookDto saveBook(BookDto bookDto);

    BookDto updateBook(Long id, BookDto bookDto);

    BookDto getBook(Long id);

    Page<BookDto> getAllBooks(int page, int size);

    String deleteBook(Long id);
}
