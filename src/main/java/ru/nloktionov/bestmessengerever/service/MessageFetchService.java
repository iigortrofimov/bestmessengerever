package ru.nloktionov.bestmessengerever.service;

import org.springframework.data.domain.PageRequest;
import ru.nloktionov.bestmessengerever.payload.response.message.MessageDetails;

import java.util.List;

public interface MessageFetchService {

    List<MessageDetails> fetchConversationMessages(Long conversationId, Long currentUserId, PageRequest pageRequest);
}
