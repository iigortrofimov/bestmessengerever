package ru.nloktionov.bestmessengerever.service.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.User;
import ru.nloktionov.bestmessengerever.enums.ConversationType;
import ru.nloktionov.bestmessengerever.exceptions.ExceptionMessage;
import ru.nloktionov.bestmessengerever.exceptions.ResourceNotFoundException;
import ru.nloktionov.bestmessengerever.repository.ConversationRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public Conversation findConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_CONVERSATION(conversationId)));
    }

    public Conversation saveConversation(Conversation conversation) {
        return conversationRepository.saveAndFlush(conversation);
    }

    public Conversation findPrivateConversationByParticipants(Set<User> users) {
        Optional<Conversation> conversationOptional =
                conversationRepository.findPrivateConversationByParticipantIdList(users.stream().map(User::getId).toList());

        if (conversationOptional.isPresent())
            return conversationOptional.get();


        Conversation conversation = buildPrivateConversation();

        Conversation conversationSaved = saveConversation(conversation);
        conversationSaved.addParticipants(users);


        return conversationSaved;
    }

    private Conversation buildPrivateConversation() {
        return Conversation.builder()
                .conversationType(ConversationType.PRIVATE)
                .build();
    }

    public List<Conversation> findConversationsByUser(User currentUser, PageRequest pageRequest) {
        return conversationRepository.findConversationsByUserId(currentUser.getId(), pageRequest);
    }

    public String defineChatName(Conversation conversation, User currentUser) {
        return conversation.getConversationType().equals(ConversationType.GROUP) ?
                conversation.getConversationMeta().getConversationName() :
                conversation.getParticipants().stream()
                        .filter(participant -> !participant.getUser().equals(currentUser))
                        .findAny().get().getUser().getFirstnameLastname();
    }
}
