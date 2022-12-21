package ru.nloktionov.bestmessengerever.service;

import ru.nloktionov.bestmessengerever.payload.request.ConversationRequest;

public interface GroupConversationCreationService {

    void processGroupConversationCreate(ConversationRequest conversationRequest, Long currentUserId);
}
