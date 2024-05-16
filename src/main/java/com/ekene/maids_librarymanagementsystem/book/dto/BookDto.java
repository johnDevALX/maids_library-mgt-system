package com.ekene.maids_librarymanagementsystem.book.dto;

import com.ekene.maids_librarymanagementsystem.book.model.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private String title;
    private String publisher;
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
    private List<String> authorEmail;
}
