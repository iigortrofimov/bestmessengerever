package ru.nloktionov.bestmessengerever.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.MessageType;
import ru.nloktionov.bestmessengerever.mapper.MessageMapper;
import ru.nloktionov.bestmessengerever.payload.request.ConversationRequest;
import ru.nloktionov.bestmessengerever.service.GroupConversationCreationService;
import ru.nloktionov.bestmessengerever.service.basic.ConversationService;
import ru.nloktionov.bestmessengerever.service.basic.MessageService;
import ru.nloktionov.bestmessengerever.service.basic.RecipientService;
import ru.nloktionov.bestmessengerever.service.basic.UserService;
import ru.nloktionov.bestmessengerever.service.websocket.WebSocketMessageSenderService;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupConversationCreationServiceImpl implements GroupConversationCreationService {
    private final UserService userService;
    private final ConversationService conversationService;
    private final MessageService messageService;
    private final WebSocketMessageSenderService webSocketMessageSenderService;
    private final MessageMapper messageMapper;
    private final RecipientService recipientService;

    @Override
    @Transactional
    public void processGroupConversationCreate(ConversationRequest conversationRequest, Long currentUserId) {
        User owner = userService.findUserById(currentUserId);
        Set<User> users = userService.findUsersById(conversationRequest.getUsersId());
        String chatName = conversationRequest.getChatName();
        Conversation conversation = conversationService.createGroupConversation(chatName, owner, users);
        Message message = messageService.buildMessage(MessageType.GROUP_CONVERSATION_STARTED, owner, conversation);
        Message messageSaved = messageService.saveMessage(message);
        users.add(owner);
        webSocketMessageSenderService.sendMessageToUsers(
                users,
                WebSocketMessageSenderService.MESSAGES_DELIVERED,
                messageMapper.toMessageDetails(message, recipientService.defineMessageStatus(messageSaved, owner)));
    }
}
