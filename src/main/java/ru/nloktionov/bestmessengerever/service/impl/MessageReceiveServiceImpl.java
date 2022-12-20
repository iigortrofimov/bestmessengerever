package ru.nloktionov.bestmessengerever.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;
import ru.nloktionov.bestmessengerever.mapper.MessageMapper;
import ru.nloktionov.bestmessengerever.service.MessageReceiveService;
import ru.nloktionov.bestmessengerever.service.basic.MessageService;
import ru.nloktionov.bestmessengerever.service.basic.PermissionService;
import ru.nloktionov.bestmessengerever.service.basic.UserService;
import ru.nloktionov.bestmessengerever.service.websocket.WebSocketMessageSenderService;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageReceiveServiceImpl implements MessageReceiveService {
    private final UserService userService;
    private final MessageService messageService;
    private final PermissionService permissionService;
    private final WebSocketMessageSenderService webSocketMessageSenderService;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public void processMessageReceive(Long messageId, Long currentUserId) {
        User user = userService.findUserById(currentUserId);
        Message message = messageService.findMessageById(messageId);

        permissionService.checkIfUserPermittedToReceiveMessage(user, message);

        User sender = message.getSender();

        message.receive(user);

        webSocketMessageSenderService.sendMessageToUsers(
                Set.of(user, sender),
                WebSocketMessageSenderService.MESSAGES_RECEIVED,
                messageMapper.toMessageDetails(message, MessageStatus.RECEIVED));
    }
}
