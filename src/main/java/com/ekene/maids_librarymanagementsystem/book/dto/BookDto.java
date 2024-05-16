package com.ekene.maids_librarymanagementsystem.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private Long id;
    private String title;
    private String publisher;
    private String isbn;
    private LocalDate publicationDate;
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
