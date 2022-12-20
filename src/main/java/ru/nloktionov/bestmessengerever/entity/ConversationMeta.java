package ru.nloktionov.bestmessengerever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "conversation_meta")
public class ConversationMeta {
    @Id
    @Column(name = "conversation_id", nullable = false)
    private Long id;

    @OneToOne()
    @MapsId
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(name = "conversation_name", nullable = false, length = 50)
    private String conversationName;

    @Column(name = "conversation_picture", nullable = false)
    private Long conversationPicture;
}