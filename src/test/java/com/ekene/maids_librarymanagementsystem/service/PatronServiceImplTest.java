package com.ekene.maids_librarymanagementsystem.service;

import com.ekene.maids_librarymanagementsystem.auth.JwtUtil;
import com.ekene.maids_librarymanagementsystem.cache.SystemCache;
import com.ekene.maids_librarymanagementsystem.patron.dto.PatronDto;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.patron.repository.PatronRepository;
import com.ekene.maids_librarymanagementsystem.patron.service.PatronServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PatronServiceImplTest {

    private PatronServiceImpl patronService;
    @Mock
    private SystemCache systemCache;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patronService = new PatronServiceImpl(patronRepository, jwtUtil, systemCache);
    }

//    @Test
//    void testAddPatron() {
//        // Arrange
//        PatronDto patronDto = new PatronDto();
//        patronDto.setFirstName("John");
//        patronDto.setLastName("Doe");
//        patronDto.setEmail("john@example.com");
//
//        // Act
//        PatronDto result = patronService.addPatron(patronDto);
//
//        // Assert
//        assertNotNull(result);
//        verify(patronRepository, times(1)).save(any(Patron.class));
//    }

    @Test
    void testAddPatron() {
        // Arrange
        PatronDto patronDto = new PatronDto();
        patronDto.setFirstName("John");
        patronDto.setLastName("Doe");
        String token = "some_token";
        String email = "john@example.com";

        Patron patron = new Patron();
        patron.setFirstName("John");
        patron.setLastName("Doe");
        patron.setEmail(email);

        JwtUtil jwtUtilMock = mock(JwtUtil.class);
        when(jwtUtilMock.extractUsername(token)).thenReturn(email);

        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        PatronServiceImpl patronService = new PatronServiceImpl(patronRepository, jwtUtilMock, systemCache);

        // Act
        PatronDto result = patronService.addPatron(patronDto, token);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(email, result.getEmail());
        verify(jwtUtilMock, times(1)).extractUsername(token);
        verify(patronRepository, times(1)).save(any(Patron.class));
    }

    @Test
    void testUpdatePatron() {
        // Arrange
        String email = "john@example.com";
        PatronDto patronDto = new PatronDto();
        patronDto.setFirstName("John");
        patronDto.setLastName("Doe");
        patronDto.setEmail("john@example.com");

        Patron existingPatron = new Patron();
        existingPatron.setFirstName("Jane");
        existingPatron.setLastName("Smith");
        existingPatron.setEmail("jane@example.com");

        Patron updatedPatron = new Patron();
        updatedPatron.setFirstName("John");
        updatedPatron.setLastName("Doe");
        updatedPatron.setEmail("john@example.com");

        when(patronRepository.findPatronByEmailIgnoreCase(email)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(updatedPatron);

        // Act
        PatronDto result = patronService.updatePatron(email, patronDto);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());
        verify(patronRepository, times(1)).findPatronByEmailIgnoreCase(email);
        verify(patronRepository, times(1)).save(any(Patron.class));
    }

    @Test
    void testGetPatron() {
        // Arrange
        String email = "john@example.com";
        Patron patron = new Patron();
        patron.setFirstName("John");
        patron.setLastName("Doe");
        patron.setEmail("john@example.com");

        when(patronRepository.findPatronByEmailIgnoreCase(email)).thenReturn(Optional.of(patron));

        // Act
        PatronDto result = patronService.getPatron(email);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void testGetAllPatrons() {
        // Arrange
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Patron> patrons = Arrays.asList(new Patron(), new Patron());
        Page<Patron> patronPage = new PageImpl<>(patrons);

        when(patronRepository.findAll(pageRequest)).thenReturn(patronPage);

        // Act
        Page<PatronDto> result = patronService.getAllPatrons(size, page);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(patronRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void testDeletePatron() {
        // Arrange
        String email = "john@example.com";
        Patron patron = new Patron();
        patron.setEmail("john@example.com");

        when(patronRepository.findPatronByEmailIgnoreCase(email)).thenReturn(Optional.of(patron));

        // Act
        String result = patronService.deletePatron(email);

        // Assert
        assertEquals("Patron account with john@example.com, successfully deleted!", result);
        verify(patronRepository, times(1)).findPatronByEmailIgnoreCase(email);
        verify(patronRepository, times(1)).delete(patron);
    }
}
