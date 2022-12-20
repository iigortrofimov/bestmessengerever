package ru.nloktionov.bestmessengerever.service.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebSocketMessageSenderService {
    private final SimpMessagingTemplate messagingTemplate;

    public static String MESSAGES_DELIVERED = "queue/messages/delivered";
    public static String MESSAGES_RECEIVED = "queue/messages/received";
    public static String USER_TYPING = "queue/users/typing";

    public void sendMessageToUsers(Set<User> users, String destination, Object message) {
        Set<String> userIdList = users.stream().map(user -> user.getId().toString()).collect(Collectors.toSet());
        userIdList.forEach(id -> messagingTemplate.convertAndSendToUser(
                id, destination, message
        ));
    }
}
