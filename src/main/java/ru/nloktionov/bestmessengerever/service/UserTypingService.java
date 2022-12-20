package ru.nloktionov.bestmessengerever.service;

import ru.nloktionov.bestmessengerever.payload.request.UserTypingRequest;

public interface UserTypingService {

    void processUserTyping(UserTypingRequest userTypingRequest, Long currentUserId);
}
