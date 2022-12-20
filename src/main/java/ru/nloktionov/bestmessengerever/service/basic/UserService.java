package ru.nloktionov.bestmessengerever.service.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.config.security.repository.UserRepository;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.exceptions.ExceptionMessage;
import ru.nloktionov.bestmessengerever.exceptions.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_USER(userId)));
    }
}
