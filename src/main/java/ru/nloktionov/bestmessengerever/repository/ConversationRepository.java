package ru.nloktionov.bestmessengerever.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nloktionov.bestmessengerever.entity.Conversation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

//    @Query(value = """
//            select c from Conversation c
//            left join c.participants cp
//            where cp.user.id in (?1) and c.conversationType = 'PRIVATE'
//            group by c.id
//            having count(c.id) > 1""")
//    Optional<Conversation> findPrivateConversationByParticipantIdList(Collection<Long> userIdList);
//
//    @Query(value = """
//            select c from Recipient r
//            join r.message m
//            join m.conversation c
//            group by c
//            order by max(m.createdAt) desc""")
//    List<Conversation> findConversationsByUserId(Long userId, PageRequest pageRequest);

    @Query(value = """
            select c.*
            from conversation c
            join participant cu on c.id = cu.conversation_id
            where user_id in (?1) and conversation_type = 'PRIVATE'
            group by c.id
            having (count(c.id) > 1)
            """, nativeQuery = true)
    Optional<Conversation> findPrivateConversationByParticipantIdList(Collection<Long> userIdList);

    @Query(value = """
            select c.*
            from conversation c
                     join (select m.conversation_id, max(created_at) timestamp
                           from recipient
                                    join message m on m.id = recipient.message_id
                           where user_id = ?1
                           group by conversation_id) as msg on msg.conversation_id = c.id
            order by timestamp desc""", nativeQuery = true)
    List<Conversation> findConversationsByUserId(Long userId, PageRequest pageRequest);
}
