package ru.nloktionov.bestmessengerever.config.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nloktionov.bestmessengerever.config.security.payload.request.LoginRequest;
import ru.nloktionov.bestmessengerever.config.security.payload.request.SignupRequest;
import ru.nloktionov.bestmessengerever.config.security.payload.response.JwtResponse;
import ru.nloktionov.bestmessengerever.config.security.payload.response.MessageAuthResponse;
import ru.nloktionov.bestmessengerever.config.security.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public MessageAuthResponse registerUser(@RequestBody SignupRequest signupRequest) {
        return authService.registerUser(signupRequest);
    }


//    @GetMapping("/jwk")
//    public Map<String, Object> keys() {
//        return this.jwkSet.toJSONObject();
//    }
}
