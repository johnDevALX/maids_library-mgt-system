package com.ekene.maids_librarymanagementsystem.book.model;

//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;

import com.ekene.maids_librarymanagementsystem.utils.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

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
//    private String author;
    private String publisher;
    @Column(unique = true)
    private String isbn;
    private LocalDateTime publicationDate;
    private String genre;
    private String language;
    private Integer numberOfPages;
    private String description;
    private Double rating;
    private Double price;
    private Boolean available;
    private Integer inventory;

    @OneToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Column(unique = false)
    private List<Author> authors;
//    private List<Category> categories;
//    private List<Review> reviews;
}
