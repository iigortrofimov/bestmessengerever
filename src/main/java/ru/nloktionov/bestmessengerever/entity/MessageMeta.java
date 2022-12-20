package ru.nloktionov.bestmessengerever.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "message_meta")
public class MessageMeta {
    @Id
    @Column(name = "message_id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversation_meta_seq")
//    @SequenceGenerator(name = "conversation_meta_seq")
    private Long id;

    @OneToOne(mappedBy = "messageMeta")
    @MapsId
    @JoinColumn(name = "message_id")
    private Message message;

    @Column(name = "content")
    private String content;

    @Column(name = "updated_conversation_name", length = 50)
    private String updated_conversation_name;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_message_id")
    private Message parentMessage;

    @ManyToOne
    @JoinColumn(name = "user_left_id_id")
    private User userLeftId;
}
