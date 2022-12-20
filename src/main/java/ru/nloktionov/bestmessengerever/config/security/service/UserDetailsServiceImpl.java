package ru.nloktionov.bestmessengerever.config.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.config.security.exceptions.UsernameNotFoundExceptionCustom;
import ru.nloktionov.bestmessengerever.config.security.model.UserDetailsImpl;
import ru.nloktionov.bestmessengerever.config.security.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundExceptionCustom("Username not found: " + username));
    }
}
