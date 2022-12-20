package ru.nloktionov.bestmessengerever.config.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.config.security.model.UserDetailsImpl;
import ru.nloktionov.bestmessengerever.config.security.payload.request.LoginRequest;
import ru.nloktionov.bestmessengerever.config.security.payload.request.SignupRequest;
import ru.nloktionov.bestmessengerever.config.security.payload.response.JwtResponse;
import ru.nloktionov.bestmessengerever.config.security.payload.response.MessageAuthResponse;
import ru.nloktionov.bestmessengerever.config.security.repository.UserRepository;
import ru.nloktionov.bestmessengerever.entity.User;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder encoder;
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = tokenService.generateToken(authentication, userDetails.getId());

        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername()
        );

        log.debug(jwtResponse.toString());

        return jwtResponse;
    }

    public MessageAuthResponse registerUser(SignupRequest signupRequest) {
        if (!userRepository.existsByUsername(signupRequest.getUsername())) {
            User user = User.builder()
                    .username(signupRequest.getUsername())
                    .password(encoder.encode(signupRequest.getPassword()))
                    .firstname(signupRequest.getFirstname())
                    .lastname(signupRequest.getLastname())
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();

            userRepository.save(user);
            return new MessageAuthResponse("User registered successfully!");
        } else {
            return new MessageAuthResponse("Error: Username is already taken!");
        }

    }
}
