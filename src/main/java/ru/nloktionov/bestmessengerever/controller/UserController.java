package ru.nloktionov.bestmessengerever.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import ru.nloktionov.bestmessengerever.payload.request.UserTypingRequest;
import ru.nloktionov.bestmessengerever.service.UserTypingService;
import ru.nloktionov.bestmessengerever.service.basic.PermissionService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserTypingService userTypingService;

    @MessageMapping("/users/typing")
    public void processUserTyping(@Payload UserTypingRequest userTypingRequest, Principal principal) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) principal;
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        Long currentUserId = (Long) jwt.getClaims().get("userId");

        userTypingService.processUserTyping(userTypingRequest, currentUserId);
    }
}
