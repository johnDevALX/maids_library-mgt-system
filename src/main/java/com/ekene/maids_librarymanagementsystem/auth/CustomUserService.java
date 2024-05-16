package com.ekene.maids_librarymanagementsystem.auth;

import com.ekene.maids_librarymanagementsystem.user.model.LmsUser;
import com.ekene.maids_librarymanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<LmsUser> user = userRepository.findLmsUserByEmailIgnoreCase(username);
        return user.map(CustomUser::new).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
