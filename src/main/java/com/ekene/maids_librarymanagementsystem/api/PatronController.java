package com.ekene.maids_librarymanagementsystem.api;

import com.ekene.maids_librarymanagementsystem.exception.PatronNotFound;
import com.ekene.maids_librarymanagementsystem.patron.dto.PatronDto;
import com.ekene.maids_librarymanagementsystem.patron.service.PatronService;
import com.ekene.maids_librarymanagementsystem.utils.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patrons")
public class PatronController extends BaseController {

    private final PatronService patronService;
    @PostMapping()
    public ResponseEntity<?> savePatron(@RequestBody PatronDto patronDto) {
        try {
            return getAppResponse(HttpStatus.CREATED, "saved successfully", patronService.addPatron(patronDto));
        } catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatron(@PathVariable Long id, @RequestBody PatronDto patronDto) {
        try {
            return getAppResponse(HttpStatus.OK, "Updated successfully", patronService.updatePatron(id, patronDto));
        }catch (PatronNotFound ex){
            return new ResponseEntity<>("Patron with this id " + id + " number not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatron(@PathVariable Long id) {
        try {
            return getAppResponse(HttpStatus.OK, "Retrieved successfully", patronService.getPatron(id));
        }catch (PatronNotFound ex){
            return new ResponseEntity<>("Patron with this id " + id + " number not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllPatrons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return getAppResponse(HttpStatus.OK, "All patrons retrieved successfully", patronService.getAllPatrons(size, page));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            return getAppResponse(HttpStatus.OK, "patron Deletion is successfully", patronService.deletePatron(id));
        }catch (PatronNotFound ex){
            return new ResponseEntity<>("Patron with this id " + id + " number not found", HttpStatus.BAD_REQUEST);
        }
    }
}
