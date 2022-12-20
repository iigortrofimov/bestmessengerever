package ru.nloktionov.bestmessengerever.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.mapper.ConversationMapper;
import ru.nloktionov.bestmessengerever.mapper.MessageMapper;
import ru.nloktionov.bestmessengerever.payload.response.chat.ConversationPreview;
import ru.nloktionov.bestmessengerever.payload.response.message.LastMessage;
import ru.nloktionov.bestmessengerever.service.FetchConversationsByUserService;
import ru.nloktionov.bestmessengerever.service.basic.ConversationService;
import ru.nloktionov.bestmessengerever.service.basic.MessageService;
import ru.nloktionov.bestmessengerever.service.basic.PermissionService;
import ru.nloktionov.bestmessengerever.service.basic.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchConversationPreviewServiceImpl implements FetchConversationsByUserService {

    private final ConversationService conversationService;
    private final UserService userService;
    private final ConversationMapper conversationMapper;
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final PermissionService permissionService;

    @Override
    public List<ConversationPreview> fetchConversations(Long currentUserId, Integer page, Integer size) {
        User currentUser = userService.findUserById(currentUserId);
        List<Conversation> conversations = new ArrayList<>(conversationService.findConversationsByUser(currentUser, PageRequest.of(page, size)));

        Collections.reverse(conversations);

        return conversations.stream().map(conversation -> {
                    LastMessage lastMessage = messageMapper
                            .toLastMessage(messageService.findLastMessageByChatId(conversation.getId(), currentUserId));

                    Integer unreadMessagesCount = messageService.countUnreadMessagesByChatId(conversation.getId(), currentUserId);

                    String conversationName = conversationService.defineChatName(conversation, currentUser);

                    return conversationMapper.toPreview(
                            conversation,
                            lastMessage,
                            unreadMessagesCount,
                            conversationName
                    );
                })
                .toList();
    }

    public ConversationPreview fetchConversationPreviewById(Long conversationId, Long currentUserId) {
        User currentUser = userService.findUserById(currentUserId);
        Conversation conversation = conversationService.findConversationById(conversationId);

        permissionService.checkIfUserPermittedToFetchConversationPreview(currentUser, conversation);

        LastMessage lastMessage = messageMapper
                .toLastMessage(messageService.findLastMessageByChatId(conversation.getId(), currentUserId));

        Integer unreadMessagesCount = messageService.countUnreadMessagesByChatId(conversation.getId(), currentUserId);

        String conversationName = conversationService.defineChatName(conversation, currentUser);

        return conversationMapper.toPreview(
                conversation,
                lastMessage,
                unreadMessagesCount,
                conversationName
        );
    }
}
