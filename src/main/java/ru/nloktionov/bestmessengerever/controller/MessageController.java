package ru.nloktionov.bestmessengerever.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nloktionov.bestmessengerever.payload.request.MessageRequest;
import ru.nloktionov.bestmessengerever.payload.response.message.MessageDetails;
import ru.nloktionov.bestmessengerever.service.MessageFetchService;
import ru.nloktionov.bestmessengerever.service.MessageReceiveService;
import ru.nloktionov.bestmessengerever.service.MessageSendService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageSendService messageSendService;
    private final MessageFetchService messageFetchService;
    private final MessageReceiveService messageReceiveService;

    @MessageMapping("/messages/send")
    public void processMessageSend(@Payload MessageRequest messageRequest, Principal principal) {
        Long currentUserId = getUserIdFromWebsocketAuth(principal);
        messageSendService.processMessageSend(messageRequest, currentUserId);
    }

    @MessageMapping("/messages/read")
    public void processMessageReceive(@Payload Long messageId, Principal principal) {
        Long currentUserId = getUserIdFromWebsocketAuth(principal);

        messageReceiveService.processMessageReceive(messageId, currentUserId);
    }

    @GetMapping("/chats/{conversationId}/messages")
    public List<MessageDetails> fetchConversationMessages(@PathVariable Long conversationId, @RequestParam Integer page, @RequestParam Integer size) {
        Long currentUserId = getUserIdFromAuth();

        return messageFetchService.fetchConversationMessages(conversationId, currentUserId, PageRequest.of(page, size));
    }

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
}
