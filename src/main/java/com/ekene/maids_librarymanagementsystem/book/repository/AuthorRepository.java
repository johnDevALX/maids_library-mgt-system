package com.ekene.maids_librarymanagementsystem.book.repository;

import com.ekene.maids_librarymanagementsystem.book.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE LOWER(a.email) IN :emails")
    List<Author> findAllByEmailIgnoreCase(@Param("emails") List<String> emails);
}
