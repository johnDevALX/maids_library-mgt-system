package com.ekene.maids_librarymanagementsystem.record.repository;

import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.record.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRepository extends JpaRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookAndPatron(Book book, Patron patron);
}
