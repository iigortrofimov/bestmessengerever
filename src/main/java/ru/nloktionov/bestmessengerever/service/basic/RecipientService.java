package ru.nloktionov.bestmessengerever.service.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.entity.Recipient;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;
import ru.nloktionov.bestmessengerever.enums.MessageType;
import ru.nloktionov.bestmessengerever.exceptions.ExceptionMessage;
import ru.nloktionov.bestmessengerever.exceptions.UserIsNotRecipientOfMessage;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipientService {

    public MessageStatus defineMessageStatus(Message message, User currentUser) {
//        User sender = message.getSender();
//        boolean isReceived;
//        if (!message.getMessageType().equals(MessageType.COMMON) | !sender.equals(currentUser)) {
//            isReceived = recipientRepository.isIncomingMessageReceived(messageModel.getId(), currentUserId);
//        } else {
//            isReceived = recipientRepository.isOutgoingMessageReceived(messageModel.getId(), currentUserId);
//        }

        User sender = message.getSender();

        boolean isReceived;

        if (!message.getMessageType().equals(MessageType.COMMON) | !sender.equals(currentUser)) {
            Recipient incomingCase = message.getRecipients().stream().filter(recipient -> recipient.getUser().equals(currentUser)).findAny()
                    .orElseThrow(() -> new UserIsNotRecipientOfMessage(ExceptionMessage.NOT_RECIPIENT(currentUser.getId(), message.getId())));

            isReceived = incomingCase.getMessageStatus().equals(MessageStatus.RECEIVED);
        } else {
            Optional<Recipient> outgoingCase = message.getRecipients().stream().filter(recipient -> (
                    recipient.getMessageStatus().equals(MessageStatus.RECEIVED) &&
                            !recipient.getUser().equals(currentUser))).findAny();

            isReceived = outgoingCase.isPresent();
        }

        return isReceived ? MessageStatus.RECEIVED : MessageStatus.DELIVERED;
    }
}
