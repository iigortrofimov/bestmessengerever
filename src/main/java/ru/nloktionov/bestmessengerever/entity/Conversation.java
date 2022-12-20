package ru.nloktionov.bestmessengerever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;
import ru.nloktionov.bestmessengerever.enums.ConversationType;
import ru.nloktionov.bestmessengerever.enums.ParticipantRole;
import ru.nloktionov.bestmessengerever.enums.ParticipantStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "conversation")
public class Conversation extends AbstractAggregateRoot<Conversation> {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversation_seq")
    @SequenceGenerator(name = "conversation_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "conversation_type", nullable = false)
    private ConversationType conversationType;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private ConversationMeta conversationMeta;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Participant> participants = new LinkedHashSet<>();

//    public void addParticipants(Set<User> users) {
//        registerEvent(new AddParticipantsToConversationEvent(this, users));
//    }
//
//    public void addParticipant() {
//
//    }
//
//    public void setParticipants(Set<Participant> participants) {
//        if (this.participants != null) {
//            this.participants.forEach(participant -> participant.setConversation(null));
//        }
//        if (participants != null) {
//            participants.forEach(participant -> participant.setConversation(this));
//        }
//        this.participants = participants;
//    }
//
    public void addParticipants(Set<User> users) {
        checkThisParticipantsNotNull();
        Set<Participant> participants = new LinkedHashSet<>();
        users.forEach(user -> participants.add(
                Participant.builder()
                        .conversation(this)
                        .user(user)
                        .participantStatus(ParticipantStatus.ACTIVE)
                        .participantRole(ParticipantRole.PARTICIPANT)
                        .build()
        ));
        this.participants.addAll(participants);
    }

    private void checkThisParticipantsNotNull() {
        if (this.participants == null)
            this.participants = new LinkedHashSet<>();
    }
}