package ru.nloktionov.bestmessengerever.service.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.entity.Participant;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;
import ru.nloktionov.bestmessengerever.enums.MessageType;
import ru.nloktionov.bestmessengerever.enums.ParticipantStatus;
import ru.nloktionov.bestmessengerever.exceptions.ExceptionMessage;
import ru.nloktionov.bestmessengerever.exceptions.ResourceNotFoundException;
import ru.nloktionov.bestmessengerever.repository.MessageRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message buildMessage(MessageType messageType, User sender, Conversation conversation, String content) {
        Message message = Message.builder()
                .messageType(messageType)
                .conversation(conversation)
                .sender(sender)
                .build();

        message.addMeta(content);

        message.addParticipants(conversation.getParticipants().stream()
                .filter(participant -> participant.getParticipantStatus().equals(ParticipantStatus.ACTIVE))
                .map(Participant::getUser)
                .collect(Collectors.toSet()));

        return message;
    }

    public Message buildMessage(MessageType messageType, User sender, Conversation conversation, String content, Message parentMessage) {
        Message message = Message.builder()
                .messageType(messageType)
                .conversation(conversation)
                .sender(sender)
                .build();

        message.addMeta(content, parentMessage);

        message.addParticipants(conversation.getParticipants().stream()
                .filter(participant -> participant.getParticipantStatus().equals(ParticipantStatus.ACTIVE))
                .map(Participant::getUser)
                .collect(Collectors.toSet()));

        return message;
    }

    public Message findMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_MESSAGE(messageId)));
    }

    public Message saveMessage(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    public Integer countUnreadMessagesByChatId(Long conversationId, Long currentUserId) {
        return messageRepository.countMessageByConversation_IdAndRecipients_User_IdAndRecipients_MessageStatus(
                conversationId,
                currentUserId,
                MessageStatus.DELIVERED);
    }

    public Message findLastMessageByChatId(Long conversationId, Long currentUserId) {
        return messageRepository.findTopMessageByConversation_IdAndRecipients_User_IdOrderByCreatedAtDesc(conversationId, currentUserId)
                .orElse(null);
    }

    public List<Message> findMessagesByConversationId(Long conversationId, Long currentUserId) {
        List<Message> messages = new ArrayList<>(messageRepository.findMessagesByConversation_IdAndRecipients_User_IdOrderByCreatedAtDesc(conversationId, currentUserId));

        Collections.reverse(messages);

        return messages;
    }
}
