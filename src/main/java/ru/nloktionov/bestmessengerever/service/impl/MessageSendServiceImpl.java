package ru.nloktionov.bestmessengerever.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.entity.Recipient;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.MessageType;
import ru.nloktionov.bestmessengerever.exceptions.ExceptionMessage;
import ru.nloktionov.bestmessengerever.exceptions.MessageRequestNotIncludeChatIdOrRecipientId;
import ru.nloktionov.bestmessengerever.mapper.MessageMapper;
import ru.nloktionov.bestmessengerever.payload.request.MessageRequest;
import ru.nloktionov.bestmessengerever.service.MessageSendService;
import ru.nloktionov.bestmessengerever.service.basic.ConversationService;
import ru.nloktionov.bestmessengerever.service.basic.MessageService;
import ru.nloktionov.bestmessengerever.service.basic.PermissionService;
import ru.nloktionov.bestmessengerever.service.basic.RecipientService;
import ru.nloktionov.bestmessengerever.service.basic.UserService;
import ru.nloktionov.bestmessengerever.service.websocket.WebSocketMessageSenderService;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageSendServiceImpl implements MessageSendService {
    private final ConversationService conversationService;
    private final UserService userService;
    private final PermissionService permissionService;
    private final MessageService messageService;
    private final WebSocketMessageSenderService webSocketMessageSenderService;
    private final MessageMapper messageMapper;

    private final RecipientService recipientService;

    @Override
    @Transactional
    public void processMessageSend(MessageRequest messageRequest, Long currentUserId) {
        User sender = userService.findUserById(currentUserId);

        Conversation conversation;

        if (messageRequest.getChatId() != null) {
            conversation = conversationService.findConversationById(messageRequest.getChatId());
            permissionService.checkIfUserPermittedToSendMessageToConversation(sender, conversation);
        } else if (messageRequest.getRecipientId() != null) {
            User receiver = userService.findUserById(messageRequest.getRecipientId());
            conversation = conversationService.findPrivateConversationByParticipants(Set.of(sender, receiver));
        } else
            throw new MessageRequestNotIncludeChatIdOrRecipientId(ExceptionMessage.MESSAGE_REQUEST_MUST_INCLUDE_CHAT_ID_OR_RECIPIENT_ID);

        String content = messageRequest.getContent();

        Message message;

        if (messageRequest.getParentMessageId() == null)
            message = messageService.buildMessage(MessageType.COMMON, sender, conversation, content);
        else {
            Message parentMessage = messageService.findMessageById(messageRequest.getParentMessageId());
            message = messageService.buildMessage(MessageType.COMMON, sender, conversation, content, parentMessage);
        }

        Message savedMessage = messageService.saveMessage(message);

        webSocketMessageSenderService.sendMessageToUsers(
                savedMessage.getRecipients().stream()
                        .map(Recipient::getUser)
                        .collect(Collectors.toSet()),
                WebSocketMessageSenderService.MESSAGES_DELIVERED,
                messageMapper.toMessageDetails(savedMessage, recipientService.defineMessageStatus(message, sender)));
    }
}
