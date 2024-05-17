package com.ekene.maids_librarymanagementsystem.book.model;

import com.ekene.maids_librarymanagementsystem.book.dto.AuthorDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

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
