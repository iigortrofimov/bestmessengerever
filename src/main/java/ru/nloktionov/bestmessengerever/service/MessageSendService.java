package ru.nloktionov.bestmessengerever.service;

import ru.nloktionov.bestmessengerever.payload.request.MessageRequest;

public interface MessageSendService {
    void processMessageSend(MessageRequest messageRequest, Long currentUserId);
}
