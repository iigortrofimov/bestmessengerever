package ru.nloktionov.bestmessengerever.service;

public interface MessageReceiveService {

    void processMessageReceive(Long messageId, Long currentUserId);
}
