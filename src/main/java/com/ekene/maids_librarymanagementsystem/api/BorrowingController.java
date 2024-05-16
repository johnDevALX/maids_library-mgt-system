package com.ekene.maids_librarymanagementsystem.api;

import com.ekene.maids_librarymanagementsystem.exception.BookNotAvailableException;
import com.ekene.maids_librarymanagementsystem.exception.PatronBorrowLimitExceededException;
import com.ekene.maids_librarymanagementsystem.record.service.BorrowingService;
import com.ekene.maids_librarymanagementsystem.utils.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BorrowingController extends BaseController {
    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            return getAppResponse(HttpStatus.OK, "Success", borrowingService.borrowBook(bookId, patronId));
        } catch (BookNotAvailableException | PatronBorrowLimitExceededException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return getAppResponse(HttpStatus.OK, "returned successfully", borrowingService.returnBook(bookId, patronId, LocalDate.now()));
    }
}
