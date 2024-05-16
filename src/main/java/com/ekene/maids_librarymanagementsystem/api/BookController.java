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
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController extends BaseController {
    private final BookService bookService;
    private final AuthorService authorService;

    @PostMapping()
    public ResponseEntity<?> saveBook(@RequestBody BookDto bookDto) {
        return getAppResponse(HttpStatus.CREATED, "saved successfully", bookService.saveBook(bookDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        return getAppResponse(HttpStatus.OK, "Updated successfully", bookService.updateBook(id, bookDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            return getAppResponse(HttpStatus.OK, "Retrieved successfully", bookService.getBook(id));
        } catch (BookNotFound ex){
            return new ResponseEntity<>("Book with this id " + id + " number not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return getAppResponse(HttpStatus.OK, "All books retrieved successfully", bookService.getAllBooks(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        return getAppResponse(HttpStatus.OK, "book Deletion is successfully", bookService.deleteBook(id));
    }

    @PostMapping("/add/author")
    public ResponseEntity<?> addAuthor(@RequestBody AuthorDto authorDto){
        return getAppResponse(HttpStatus.OK, "Author added successfully", authorService.saveAuthor(authorDto));
    }
}
