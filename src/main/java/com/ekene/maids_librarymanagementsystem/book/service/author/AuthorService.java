package com.ekene.maids_librarymanagementsystem.book.service.author;

import com.ekene.maids_librarymanagementsystem.book.dto.AuthorDto;
import com.ekene.maids_librarymanagementsystem.book.model.Author;
import com.ekene.maids_librarymanagementsystem.book.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Author saveAuthor(AuthorDto authorDto){
        Author author = new Author();
        author.save(authorDto);
        return authorRepository.save(author);
    }
}
