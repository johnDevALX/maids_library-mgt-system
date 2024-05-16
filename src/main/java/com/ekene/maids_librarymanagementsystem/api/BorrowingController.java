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
@RequestMapping("/v1/record/")
public class BorrowingController extends BaseController {
    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{isbn}/patron/{email}")
    public ResponseEntity<?> borrowBook(@PathVariable String isbn, @PathVariable String email) {
        try {
            return getAppResponse(HttpStatus.OK, "Success", borrowingService.borrowBook(isbn, email));
        } catch (BookNotAvailableException | PatronBorrowLimitExceededException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/return/{isbn}/patron/{email}")
    public ResponseEntity<?> returnBook(@PathVariable String isbn, @PathVariable String email) {
        return getAppResponse(HttpStatus.OK, "returned successfully", borrowingService.returnBook(isbn, email, LocalDate.now()));
    }
}
