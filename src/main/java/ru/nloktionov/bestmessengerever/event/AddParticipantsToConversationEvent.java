package ru.nloktionov.bestmessengerever.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.nloktionov.bestmessengerever.entity.Conversation;
import ru.nloktionov.bestmessengerever.entity.User;

import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
public class AddParticipantsToConversationEvent {
    private Conversation conversation;
    private Set<User> users;
}
