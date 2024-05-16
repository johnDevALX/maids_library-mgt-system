package com.ekene.maids_librarymanagementsystem.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDto {
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private String nationality;
}
