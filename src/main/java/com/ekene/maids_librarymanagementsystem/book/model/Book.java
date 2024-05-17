package com.ekene.maids_librarymanagementsystem.book.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String title;
    private String publisher;
    @Column(unique = true)
    private String isbn;
    private LocalDate publicationDate;
    private String genre;
    private String language;
    private Integer numberOfPages;
    private String description;
    private Double rating;
    private Boolean available;
    private Integer inventory;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"book_id", "author_id"})
    )
    private List<Author> authors = new ArrayList<>();
    @PostLoad
    private void initAuthors() {
        if (authors == null) {
            authors = new ArrayList<>();
        }
    }
}
