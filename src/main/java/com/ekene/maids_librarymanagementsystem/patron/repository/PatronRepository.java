package com.ekene.maids_librarymanagementsystem.patron.repository;

import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {

    Optional<Patron> findPatronByEmailIgnoreCase(String email);

    Page<Patron> findAll(Pageable pageable);
}
