package ru.nloktionov.bestmessengerever.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    Optional<Message> findTopMessageByConversation_IdAndRecipients_User_IdOrderByCreatedAtDesc(Long conversationId, Long userId);

    Integer countMessageByConversation_IdAndRecipients_User_IdAndRecipients_MessageStatus(Long conversationId, Long userId, MessageStatus messageStatus);

    List<Message> findMessagesByConversation_IdAndRecipients_User_IdOrderByCreatedAtDesc(Long conversationId, Long userId);
}
