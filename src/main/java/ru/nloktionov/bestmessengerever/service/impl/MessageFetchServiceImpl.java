package ru.nloktionov.bestmessengerever.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;
import ru.nloktionov.bestmessengerever.mapper.MessageMapper;
import ru.nloktionov.bestmessengerever.payload.response.message.MessageDetails;
import ru.nloktionov.bestmessengerever.service.MessageFetchService;
import ru.nloktionov.bestmessengerever.service.basic.ConversationService;
import ru.nloktionov.bestmessengerever.service.basic.MessageService;
import ru.nloktionov.bestmessengerever.service.basic.PermissionService;
import ru.nloktionov.bestmessengerever.service.basic.RecipientService;
import ru.nloktionov.bestmessengerever.service.basic.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageFetchServiceImpl implements MessageFetchService {
    private final UserService userService;
    private final ConversationService conversationService;
    private final MessageService messageService;
    private final PermissionService permissionService;
    private final MessageMapper messageMapper;
    private final RecipientService recipientService;

    @Override
    public List<MessageDetails> fetchConversationMessages(Long conversationId, Long currentUserId, PageRequest pageRequest) {
        User currentUser = userService.findUserById(currentUserId);
        Conversation conversation = conversationService.findConversationById(conversationId);

        permissionService.checkIfUserPermittedToFetchConversationMessages(currentUser, conversation);

        return messageService.findMessagesByConversationId(conversationId, currentUserId).stream()
                .map(message -> messageMapper.toMessageDetails(message, recipientService.defineMessageStatus(message, currentUser)))
                .collect(Collectors.toList());
    }
}
