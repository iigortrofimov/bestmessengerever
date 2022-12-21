package ru.nloktionov.bestmessengerever.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nloktionov.bestmessengerever.payload.request.ConversationRequest;
import ru.nloktionov.bestmessengerever.payload.response.chat.ConversationPreview;
import ru.nloktionov.bestmessengerever.service.GroupConversationCreationService;
import ru.nloktionov.bestmessengerever.service.FetchConversationsByUserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConversationController {

    private final FetchConversationsByUserService fetchConversationsByUserService;
    private final GroupConversationCreationService groupConversationCreationService;

    private Long getUserIdFromAuth() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        return (Long) jwt.getClaims().get("userId");
    }

    private Long getUserIdFromWebsocketAuth(Principal principal) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) principal;
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        return (Long) jwt.getClaims().get("userId");
    }

    @GetMapping("/chats")
    private List<ConversationPreview> fetchConversationsByUser(@RequestParam Integer page, @RequestParam Integer size) {
        Long currentUserId = getUserIdFromAuth();

        return fetchConversationsByUserService.fetchConversations(currentUserId, page, size);
    }

    @GetMapping("/chats/{conversationId}")
    private ConversationPreview fetchConversationByUser(@PathVariable Long conversationId) {
        Long currentUserId = getUserIdFromAuth();

        return fetchConversationsByUserService.fetchConversationPreviewById(conversationId, currentUserId);
    }

    @MessageMapping("/chats/created")
    public void processGroupConversationCreate(@Payload ConversationRequest conversationRequest, Principal principal) {
        Long currentUserId = getUserIdFromWebsocketAuth(principal);

        groupConversationCreationService.processGroupConversationCreate(conversationRequest, currentUserId);
    }
}
