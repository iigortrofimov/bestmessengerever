package ru.nloktionov.bestmessengerever.service;

import ru.nloktionov.bestmessengerever.payload.response.chat.ConversationPreview;

import java.util.List;

public interface FetchConversationsByUserService {

    List<ConversationPreview> fetchConversations(Long currentUserId, Integer page, Integer size);

    ConversationPreview fetchConversationPreviewById(Long conversationId, Long currentUserId);
}
