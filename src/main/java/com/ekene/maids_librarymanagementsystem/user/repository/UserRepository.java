package com.ekene.maids_librarymanagementsystem.user.repository;

import com.ekene.maids_librarymanagementsystem.user.model.LmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<LmsUser, Long> {
    Optional<LmsUser> findLmsUserByEmailIgnoreCase(String email);
}
