package ru.nloktionov.bestmessengerever.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.Participant;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.ParticipantStatus;
import ru.nloktionov.bestmessengerever.payload.request.UserTypingRequest;
import ru.nloktionov.bestmessengerever.payload.response.UserTypingResponse;
import ru.nloktionov.bestmessengerever.service.UserTypingService;
import ru.nloktionov.bestmessengerever.service.basic.ConversationService;
import ru.nloktionov.bestmessengerever.service.basic.UserService;
import ru.nloktionov.bestmessengerever.service.websocket.WebSocketMessageSenderService;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTypingServiceImpl implements UserTypingService {
    private final UserService userService;
    private final ConversationService conversationService;
    private final WebSocketMessageSenderService webSocketMessageSenderService;

    @Override
    @Transactional
    public void processUserTyping(UserTypingRequest userTypingRequest, Long currentUserId) {
        User user = userService.findUserById(currentUserId);
        Conversation conversation = conversationService.findConversationById(userTypingRequest.getChatId());
        Boolean isTyping = userTypingRequest.getIsTyping();

        Set<User> users = conversation.getParticipants().stream().filter(
                        participant -> !participant.getUser().equals(user) &&
                                participant.getParticipantStatus().equals(ParticipantStatus.ACTIVE))
                .map(Participant::getUser)
                .collect(Collectors.toSet());

        webSocketMessageSenderService.sendMessageToUsers(
                users,
                WebSocketMessageSenderService.USER_TYPING,
                new UserTypingResponse(conversation.getId(), user.getFirstname(), isTyping));
    }
}
