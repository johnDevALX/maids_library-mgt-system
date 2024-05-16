package com.ekene.maids_librarymanagementsystem.api;

import com.ekene.maids_librarymanagementsystem.book.dto.AuthorDto;
import com.ekene.maids_librarymanagementsystem.book.dto.BookDto;
import com.ekene.maids_librarymanagementsystem.book.service.BookService;
import com.ekene.maids_librarymanagementsystem.book.service.author.AuthorService;
import com.ekene.maids_librarymanagementsystem.exception.BookNotFound;
import com.ekene.maids_librarymanagementsystem.utils.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController extends BaseController {
    private final BookService bookService;
    private final AuthorService authorService;

    @PostMapping("/create")
    public ResponseEntity<?> saveBook(@RequestBody BookDto bookDto) {
        return getAppResponse(HttpStatus.CREATED, "saved successfully", bookService.saveBook(bookDto));
    }

    @PutMapping("/update/{isbn}")
    public ResponseEntity<?> updateBook(@PathVariable String isbn, @RequestBody BookDto bookDto) {
        return getAppResponse(HttpStatus.OK, "Updated successfully", bookService.updateBook(isbn, bookDto));
    }

    @GetMapping("/get/{isbn}")
    public ResponseEntity<?> getBook(@PathVariable String isbn) {
        try {
            return getAppResponse(HttpStatus.OK, "Retrieved successfully", bookService.getBook(isbn));
        } catch (BookNotFound ex){
            return new ResponseEntity<>("Book with this isbn " + isbn + " number not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return getAppResponse(HttpStatus.OK, "All books retrieved successfully", bookService.getAllBooks(page, size));
    }

    @DeleteMapping("/delete/{isbn}")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn) {
        return getAppResponse(HttpStatus.OK, "book Deletion is successfully", bookService.deleteBook(isbn));
    }

    @PostMapping("/add/author")
    public ResponseEntity<?> addAuthor(@RequestBody AuthorDto authorDto){
        return getAppResponse(HttpStatus.OK, "Author added successfully", authorService.saveAuthor(authorDto));
    }
}
