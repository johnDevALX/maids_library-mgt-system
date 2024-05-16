package com.ekene.maids_librarymanagementsystem.patron.service;

import com.ekene.maids_librarymanagementsystem.patron.dto.PatronDto;
import org.springframework.data.domain.Page;

public interface PatronService {
    PatronDto addPatron(PatronDto patronDto, String token);

    PatronDto updatePatron(Long id, PatronDto patronDto);

    PatronDto getPatron(Long id);

    Page<PatronDto> getAllPatrons(int size, int page);

    String deletePatron(Long id);
}
