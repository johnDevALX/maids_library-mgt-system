package com.ekene.maids_librarymanagementsystem.record.model;

import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.utils.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BorrowingRecord extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

//    @Column(name = "borrowed_date", nullable = false)
    private LocalDate borrowedDate;

//    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

//    @Column(name = "returned_date")
    private LocalDate returnedDate;

}
