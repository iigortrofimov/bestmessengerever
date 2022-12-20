package ru.nloktionov.bestmessengerever.exceptions;

public class MessageRequestNotIncludeChatIdOrRecipientId extends RuntimeException {
    public MessageRequestNotIncludeChatIdOrRecipientId(String message) {
        super(message);
    }
}
