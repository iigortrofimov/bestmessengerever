package ru.nloktionov.bestmessengerever.service.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.Message;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.ParticipantStatus;
import ru.nloktionov.bestmessengerever.exceptions.ExceptionMessage;
import ru.nloktionov.bestmessengerever.exceptions.UserIsNotActiveMemberOfChatException;
import ru.nloktionov.bestmessengerever.exceptions.UserIsNotMemberOfChatException;
import ru.nloktionov.bestmessengerever.exceptions.UserIsNotRecipientOfMessage;

@Service
@RequiredArgsConstructor
public class PermissionService {

    public void checkIfUserPermittedToSendMessageToConversation(User sender, Conversation conversation) {
        checkIfUserIsActiveParticipantOfConversation(sender, conversation);
    }

    private void checkIfUserIsActiveParticipantOfConversation(User sender, Conversation conversation) {
        conversation.getParticipants().stream()
                .filter(participant ->
                        participant.getUser().equals(sender) &&
                                participant.getParticipantStatus().equals(ParticipantStatus.ACTIVE))
                .findAny().orElseThrow(() ->
                        new UserIsNotActiveMemberOfChatException(ExceptionMessage.NOT_ACTIVE_MEMBER(sender.getId(), conversation.getId())));
    }

    private void checkIfUserIsParticipantOfConversation(User user, Conversation conversation) {
        conversation.getParticipants().stream().filter(participant -> participant.getUser().equals(user))
                .findAny()
                .orElseThrow(() -> new UserIsNotMemberOfChatException(ExceptionMessage.NOT_MEMBER(user.getId(), conversation.getId())));

    }


    public void checkIfUserPermittedToFetchConversationPreview(User user, Conversation conversation) {
        checkIfUserIsParticipantOfConversation(user, conversation);
    }

    public void checkIfUserPermittedToFetchConversationMessages(User currentUser, Conversation conversation) {
        checkIfUserIsParticipantOfConversation(currentUser, conversation);
    }

    public void checkIfUserPermittedToReceiveMessage(User user, Message message) {
        checkIfUserRecipientOfMessage(user, message);
    }

    private void checkIfUserRecipientOfMessage(User user, Message message) {
        message.getRecipients().stream().filter(recipient -> recipient.getUser().equals(user)).findAny()
                .orElseThrow(() -> new UserIsNotRecipientOfMessage(ExceptionMessage.NOT_RECIPIENT(user.getId(), message.getId())));
    }
}
