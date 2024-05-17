package com.ekene.maids_librarymanagementsystem.book.model;

import com.ekene.maids_librarymanagementsystem.book.dto.AuthorDto;
import com.ekene.maids_librarymanagementsystem.utils.model.BaseModel;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Author extends BaseModel implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private String nationality;

    public void save(AuthorDto dto){
        setEmail(dto.getEmail());
        setFirstName(dto.getFirstName());
        setLastName(dto.getLastName());
        setMobile(dto.getMobile());
        setNationality(dto.getNationality());
    }
}
