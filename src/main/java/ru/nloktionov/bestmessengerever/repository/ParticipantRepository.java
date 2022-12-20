package ru.nloktionov.bestmessengerever.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nloktionov.bestmessengerever.entity.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

}
