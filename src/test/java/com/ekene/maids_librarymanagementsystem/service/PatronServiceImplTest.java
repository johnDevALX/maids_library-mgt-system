package com.ekene.maids_librarymanagementsystem.service;

import com.ekene.maids_librarymanagementsystem.auth.JwtUtil;
import com.ekene.maids_librarymanagementsystem.cache.SystemCache;
import com.ekene.maids_librarymanagementsystem.patron.dto.PatronDto;
import com.ekene.maids_librarymanagementsystem.patron.model.MembershipType;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.patron.repository.PatronRepository;
import com.ekene.maids_librarymanagementsystem.patron.service.PatronServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PatronServiceImplTest.class)
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

    @Test
    void testAddPatron() {
        PatronDto patronDto = new PatronDto();
        patronDto.setFirstName("tolu");
        patronDto.setLastName("eze");
        patronDto.setMembershipType("REGULAR");
        String token = "some_token";
        String email = "tolu@example.com";

        Patron patron = new Patron();
        patron.setFirstName("tolu");
        patron.setLastName("eze");
        patron.setEmail(email);
        patron.setMembershipType(MembershipType.REGULAR);

        JwtUtil jwtUtilMock = mock(JwtUtil.class);
        when(jwtUtilMock.extractUsername(token)).thenReturn(email);

        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        PatronServiceImpl patronService = new PatronServiceImpl(patronRepository, jwtUtilMock, systemCache);

        PatronDto result = patronService.addPatron(patronDto, token);

        assertNotNull(result);
        assertEquals("tolu", result.getFirstName());
        assertEquals("eze", result.getLastName());
        assertEquals(email, result.getEmail());
        verify(jwtUtilMock, times(1)).extractUsername(token);
        verify(patronRepository, times(1)).save(any(Patron.class));
    }

    @Test
    void testUpdatePatron() {
        Long id = 9L;
        PatronDto patronDto = new PatronDto();
        patronDto.setFirstName("tolu");
        patronDto.setLastName("eze");
        patronDto.setId(id);

        Patron existingPatron = new Patron();
        existingPatron.setFirstName("Okoro");
        existingPatron.setLastName("ola");
        existingPatron.setId(8L);

        Patron updatedPatron = new Patron();
        updatedPatron.setFirstName("tolu");
        updatedPatron.setLastName("eze");
        updatedPatron.setId(id);

        when(patronRepository.findById(id)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(updatedPatron);

        PatronDto result = patronService.updatePatron(id, patronDto);

        assertNotNull(result);
        assertEquals("tolu", result.getFirstName());
        assertEquals("eze", result.getLastName());
        assertEquals(9L, result.getId());
        verify(patronRepository, times(1)).findById(id);
        verify(patronRepository, times(1)).save(any(Patron.class));
    }

    @Test
    void testGetPatron() {
        Long id = 9L;
        Patron patron = new Patron();
        patron.setFirstName("tolu");
        patron.setLastName("eze");
        patron.setId(id);

        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));

        PatronDto result = patronService.getPatron(id);

        assertNotNull(result);
        assertEquals("tolu", result.getFirstName());
        assertEquals("eze", result.getLastName());
        assertEquals(9L, result.getId());
    }

    @Test
    void testGetAllPatrons() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Patron> patrons = Arrays.asList(new Patron(), new Patron());
        Page<Patron> patronPage = new PageImpl<>(patrons);

        when(patronRepository.findAll(pageRequest)).thenReturn(patronPage);

        Page<PatronDto> result = patronService.getAllPatrons(size, page);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(patronRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void testDeletePatron() {
        Long id = 9L;
        Patron patron = new Patron();
        patron.setId(id);

        when(patronRepository.findById(id)).thenReturn(Optional.of(patron));

        String result = patronService.deletePatron(id);

        assertEquals("Patron account with 9, successfully deleted!", result);
        verify(patronRepository, times(1)).findById(id);
        verify(patronRepository, times(1)).delete(patron);
    }
}
