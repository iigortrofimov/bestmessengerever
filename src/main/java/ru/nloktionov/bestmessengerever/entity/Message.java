package ru.nloktionov.bestmessengerever.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;
import ru.nloktionov.bestmessengerever.enums.MessageType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private MessageMeta messageMeta;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Recipient> recipients = new LinkedHashSet<>();

    public void receive(User user) {
        Recipient recipient = this.recipients.stream().filter(recipientNested -> recipientNested.getUser().equals(user)).findAny().get();
        recipient.setMessageStatus(MessageStatus.RECEIVED);
    }

    public void addParticipants(Set<User> users) {
        checkThisRecipientsNotNull();
        Set<Recipient> recipients = new LinkedHashSet<>();
        users.forEach(user -> recipients.add(
                Recipient.builder()
                        .message(this)
                        .user(user)
                        .messageStatus(user.getId().equals(sender.getId()) ? MessageStatus.RECEIVED : MessageStatus.DELIVERED)
                        .build()));

        this.recipients.addAll(recipients);
    }

    private void checkThisRecipientsNotNull() {
        if (this.recipients == null)
            this.recipients = new LinkedHashSet<>();
    }

    public void addMeta(String content) {
        this.messageMeta = MessageMeta.builder()
                .message(this)
                .content(content)
                .build();
    }

    public void addMeta(String content, Message parentMessage) {
        this.messageMeta = MessageMeta.builder()
                .content(content)
                .message(this)
                .parentMessage(parentMessage)
                .build();
    }
}