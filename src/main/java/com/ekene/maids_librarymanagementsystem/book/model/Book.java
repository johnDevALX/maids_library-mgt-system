package com.ekene.maids_librarymanagementsystem.book.model;

import com.ekene.maids_librarymanagementsystem.utils.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Book extends BaseModel {
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

    @OneToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;
}
