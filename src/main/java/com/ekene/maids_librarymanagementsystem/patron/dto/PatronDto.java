package com.ekene.maids_librarymanagementsystem.patron.dto;

import com.ekene.maids_librarymanagementsystem.patron.model.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatronDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate membershipStartDate;
    private LocalDate membershipEndDate;
    private MembershipType membershipType;
    private int borrowedBooks;
}
