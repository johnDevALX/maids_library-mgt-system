package com.ekene.maids_librarymanagementsystem.patron.service;

import com.ekene.maids_librarymanagementsystem.auth.JwtUtil;
import com.ekene.maids_librarymanagementsystem.cache.SystemCache;
import com.ekene.maids_librarymanagementsystem.exception.PatronNotFound;
import com.ekene.maids_librarymanagementsystem.patron.dto.PatronDto;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import com.ekene.maids_librarymanagementsystem.patron.repository.PatronRepository;
import com.ekene.maids_librarymanagementsystem.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatronServiceImpl implements PatronService{
    private final PatronRepository patronRepository;
    private final JwtUtil jwtUtil;
    private final SystemCache systemCache;
    @Override
    public PatronDto addPatron(PatronDto patronDto, String token) {
        String email = jwtUtil.extractUsername(token);
        patronDto.setEmail(email);
        Patron patron = patronRepository.save(JsonMapper.convertToPatron(patronDto));
        systemCache.addPatron(patron);
        return JsonMapper.convertPatronToDto(patron);
    }

    @Override
    @Transactional
    public PatronDto updatePatron(Long id, PatronDto patronDto) {
        Patron existingPatron = patronRepository.findById(id)
                .orElseThrow(() -> new PatronNotFound("Patron not found with id: " + id));

        existingPatron.setFirstName(patronDto.getFirstName() != null ? patronDto.getFirstName() : existingPatron.getFirstName());
        existingPatron.setLastName(patronDto.getLastName() != null ? patronDto.getLastName() : existingPatron.getLastName());
        existingPatron.setEmail(patronDto.getEmail() != null ? patronDto.getEmail() : existingPatron.getEmail());
        existingPatron.setPhoneNumber(patronDto.getPhoneNumber() != null ? patronDto.getPhoneNumber() : existingPatron.getPhoneNumber());
        existingPatron.setAddress(patronDto.getAddress() != null ? patronDto.getAddress() : existingPatron.getAddress());
        existingPatron.setMembershipStartDate(patronDto.getMembershipStartDate() != null ? patronDto.getMembershipStartDate() : existingPatron.getMembershipStartDate());
        existingPatron.setMembershipEndDate(patronDto.getMembershipEndDate() != null ? patronDto.getMembershipEndDate() : existingPatron.getMembershipEndDate());
        existingPatron.setMembershipType(patronDto.getMembershipType() != null ? patronDto.getMembershipType() : existingPatron.getMembershipType());
        existingPatron.setBorrowedBooks(Objects.nonNull(patronDto.getBorrowedBooks()) ? patronDto.getBorrowedBooks() : existingPatron.getBorrowedBooks());

        Patron updatedPatron = patronRepository.save(existingPatron);
        systemCache.addPatron(updatedPatron);
        return JsonMapper.convertPatronToDto(updatedPatron);
    }

    @Override
    public PatronDto getPatron(Long id) {
        Patron existingPatron = systemCache.getPatron(id);
        if (Objects.isNull(existingPatron)){
               existingPatron = patronRepository.findById(id)
                .orElseThrow(PatronNotFound::new);
        }
        return JsonMapper.convertPatronToDto(existingPatron);
    }

    @Override
    public Page<PatronDto> getAllPatrons(int size, int page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patron> all = patronRepository.findAll(pageRequest);
        List<PatronDto> patronDtos = all.getContent()
                .stream()
                .map(JsonMapper::convertPatronToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(patronDtos, pageRequest, all.getTotalElements());
    }

    @Override
    public String deletePatron(Long id) {
        Patron patron = patronRepository.findById(id)
                .orElseThrow(PatronNotFound::new);
        patronRepository.delete(patron);
        return "Patron account with " + id + ", successfully deleted!";
    }
}
