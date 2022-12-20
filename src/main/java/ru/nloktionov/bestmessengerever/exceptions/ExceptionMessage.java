package ru.nloktionov.bestmessengerever.exceptions;

import java.util.Arrays;
import java.util.Collection;

public class ExceptionMessage {
    public static String NOT_FOUND_CONVERSATION(Long conversationId) {
        return String.format("Not found conversation with id %d", conversationId);
    }

    public static String NOT_FOUND_CONVERSATION_WITH_TYPE_PRIVATE_AND_USERS_ID_IN(Collection<Long> userIdList) {
        return String.format("Not found private conversation with userIds %s", Arrays.toString(userIdList.toArray()));
    }

    public static String NOT_FOUND_USER(Long userId) {
        return String.format("Not found user with id %d", userId);
    }

    public static String NOT_ACTIVE_MEMBER(Long userId, Long chatId) {
        return String.format("User with id %d is not an active member of a chat with id %d", userId, chatId);
    }
    public static String MESSAGE_REQUEST_MUST_INCLUDE_CHAT_ID_OR_RECIPIENT_ID = "Message request must include chatId or recipientId";

    public static String NOT_FOUND_MESSAGE(Long messageId) {
        return String.format("Not found message with id %d", messageId);
    }

    public static String NOT_MEMBER(Long userId, Long conversationId) {
        return String.format("User with id %d is not an member of a chat with id %d", userId, conversationId);
    }

    public static String NOT_RECIPIENT(Long userId, Long messageId) {
        return String.format("User with id %d is not an recipient of a message with id %d", messageId, messageId);
    }
}
