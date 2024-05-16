package com.ekene.maids_librarymanagementsystem.book.service;

import com.ekene.maids_librarymanagementsystem.book.dto.BookDto;
import com.ekene.maids_librarymanagementsystem.book.model.Author;
import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.book.repository.AuthorRepository;
import com.ekene.maids_librarymanagementsystem.book.repository.BookRepository;
import com.ekene.maids_librarymanagementsystem.cache.SystemCache;
import com.ekene.maids_librarymanagementsystem.exception.BookNotFound;
import com.ekene.maids_librarymanagementsystem.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final SystemCache systemCache;

    @Override
    public BookDto saveBook(BookDto bookDto) {
        log.info("author emails found [{}]", bookDto.getAuthorEmail());

        List<Author> authors = new ArrayList<>(authorRepository.findAllByEmailIgnoreCase(bookDto.getAuthorEmail()));
        log.info("author found [{}]", authors);
        Book book = bookRepository.save(JsonMapper.convertBookDtoToEntity(bookDto, authors));
        systemCache.addBook(book);
        return JsonMapper.convertBookToDto(book);
    }

    @Override
    @Transactional
    public BookDto updateBook(String isbn, BookDto bookDto) {
        Book existingBook = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new BookNotFound("Book not found with ISBN: " + isbn));

        List<Author> authors = new ArrayList<>(authorRepository.findAllByEmailIgnoreCase(bookDto.getAuthorEmail()));

        existingBook.setTitle(bookDto.getTitle() != null ? bookDto.getTitle() : existingBook.getTitle());
        existingBook.setPublisher(bookDto.getPublisher() != null ? bookDto.getPublisher() : existingBook.getPublisher());
        existingBook.setIsbn(bookDto.getIsbn() != null ? bookDto.getIsbn() : existingBook.getIsbn());
        existingBook.setPublicationDate(bookDto.getPublicationDate() != null ? bookDto.getPublicationDate() : existingBook.getPublicationDate());
        existingBook.setGenre(bookDto.getGenre() != null ? bookDto.getGenre() : existingBook.getGenre());
        existingBook.setLanguage(bookDto.getLanguage() != null ? bookDto.getLanguage() : existingBook.getLanguage());
        existingBook.setNumberOfPages(bookDto.getNumberOfPages() != null ? bookDto.getNumberOfPages() : existingBook.getNumberOfPages());
        existingBook.setDescription(bookDto.getDescription() != null ? bookDto.getDescription() : existingBook.getDescription());
        existingBook.setRating(bookDto.getRating() != null ? bookDto.getRating() : existingBook.getRating());
        existingBook.setPrice(bookDto.getPrice() != null ? bookDto.getPrice() : existingBook.getPrice());
        existingBook.setAuthors(authors);
        existingBook.setAvailable(bookDto.getAvailable() != null ? bookDto.getAvailable() : existingBook.getAvailable());
        existingBook.setInventory(bookDto.getInventory() != null ? bookDto.getInventory() : existingBook.getInventory());

        Book savedBook = bookRepository.save(existingBook);
        systemCache.addBook(savedBook);
        return JsonMapper.convertBookToDto(savedBook);
    }

    @Override
    public BookDto getBook(String isbn) {
        Book book = systemCache.getBook(isbn);
        if (Objects.isNull(book)){
            book = bookRepository.findBookByIsbn(isbn).orElseThrow(BookNotFound::new);
        }
        return JsonMapper.convertBookToDto(book);
    }

    @Override
    public Page<BookDto> getAllBooks(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Page request [{}]", pageRequest);
        Page<Book> all = bookRepository.findAll(pageRequest);

        log.info("Page returned [{}]", all);

        List<BookDto> bookDtos = all.getContent()
                .stream()
                .map(JsonMapper::convertBookToDto)
                .toList();

        log.info("List of books returned [{}]", bookDtos);
        return new PageImpl<>(bookDtos, pageRequest, all.getTotalElements());
    }

    @Override
    public String deleteBook(String isbn) {
        Book book = bookRepository.findBookByIsbn(isbn).orElseThrow(BookNotFound::new);
        bookRepository.delete(book);
        return "Book successfully deleted!";
    }
}
