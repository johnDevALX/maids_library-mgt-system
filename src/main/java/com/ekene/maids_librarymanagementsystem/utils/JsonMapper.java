package com.ekene.maids_librarymanagementsystem.utils;

import com.ekene.maids_librarymanagementsystem.book.dto.BookDto;
import com.ekene.maids_librarymanagementsystem.book.model.Author;
import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.patron.dto.PatronDto;
import com.ekene.maids_librarymanagementsystem.patron.model.MembershipType;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JsonMapper {
    public static BookDto convertBookToDto(Book book){
        BookDto.BookDtoBuilder builder = BookDto.builder();

        builder.id(book.getId())
                .genre(book.getGenre())
                .isbn(book.getIsbn())
                .available(book.getAvailable())
                .description(book.getDescription())
                .title(book.getTitle())
                .inventory(book.getInventory())
                .language(book.getLanguage())
                .numberOfPages(book.getNumberOfPages())
                .publisher(book.getPublisher())
                .rating(book.getRating())
                .authorEmail(book.getAuthors().stream()
                        .map(Author::getEmail)
                        .toList())
                .publicationDate(book.getPublicationDate());

        return builder.build();
    }

    public static Book convertBookDtoToEntity(BookDto book, List<Author> authors){
        Book.BookBuilder builder = Book.builder();

        builder.genre(book.getGenre());
        builder.isbn(book.getIsbn());
        builder.available(book.getAvailable());
        builder.description(book.getDescription());
        builder.title(book.getTitle());
        builder.inventory(book.getInventory());
        builder.language(book.getLanguage());
        builder.numberOfPages(book.getNumberOfPages());
        builder.publisher(book.getPublisher());
        builder.rating(book.getRating());
        builder.authors(authors);
        builder.publicationDate(book.getPublicationDate());

        return builder.build();
    }

    public static PatronDto convertPatronToDto(Patron patron){
        PatronDto.PatronDtoBuilder builder = PatronDto.builder();

        builder.id(patron.getId());
        builder.firstName(patron.getFirstName());
        builder.lastName(patron.getLastName());
        builder.email(patron.getEmail());
        builder.phoneNumber(patron.getPhoneNumber());
        builder.address(patron.getAddress());
        builder.membershipStartDate(patron.getMembershipStartDate());
        builder.membershipEndDate(patron.getMembershipEndDate());
        builder.membershipType(patron.getMembershipType());
        builder.borrowedBooks(patron.getBorrowedBooks());
        return builder.build();
    }

    public static Patron convertToPatron(PatronDto patron){
        MembershipType membershipType = patron.getMembershipType();
        Patron.PatronBuilder builder = Patron.builder();
        builder.firstName(patron.getFirstName());
        builder.lastName(patron.getLastName());
        builder.email(patron.getEmail());
        builder.phoneNumber(patron.getPhoneNumber());
        builder.address(patron.getAddress());
        builder.membershipStartDate(patron.getMembershipStartDate());
        builder.membershipEndDate(patron.getMembershipEndDate());
        builder.membershipType(membershipType);
        builder.borrowedBooks(patron.getBorrowedBooks());
        return builder.build();
    }
}
