package com.ekene.maids_librarymanagementsystem.book.repository;

import com.ekene.maids_librarymanagementsystem.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAll(Pageable pageable);

    Optional<Book> findBookByIsbn(String isbn);
}
