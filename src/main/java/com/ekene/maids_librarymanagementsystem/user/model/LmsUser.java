package com.ekene.maids_librarymanagementsystem.user.model;

import com.ekene.maids_librarymanagementsystem.utils.model.BaseModel;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LmsUser extends BaseModel {
    private String email;
    private String password;
    private String role;
}
