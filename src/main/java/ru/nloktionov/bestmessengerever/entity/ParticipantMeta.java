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
@Table(name = "participant_meta")
public class ParticipantMeta {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participant_meta_seq")
//    @SequenceGenerator(name = "participant_meta_seq")
    @Column(name = "participant_id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @Column(name = "last_known_name", nullable = false, length = 50)
    private String lastKnownName;

    @Column(name = "last_known_picture")
    private Long lastKnownPicture;
}