package com.ekene.maids_librarymanagementsystem.patron.model;

import com.ekene.maids_librarymanagementsystem.utils.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Patron extends BaseModel {
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate membershipStartDate;
    private LocalDate membershipEndDate;
    private MembershipType membershipType;
//    private boolean isActive;
    private int borrowedBooks;
//    private int maxAllowedBooks;
}
