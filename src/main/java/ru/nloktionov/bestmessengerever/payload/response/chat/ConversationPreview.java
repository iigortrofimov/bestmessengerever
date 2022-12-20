package ru.nloktionov.bestmessengerever.payload.response.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.nloktionov.bestmessengerever.enums.ConversationType;
import ru.nloktionov.bestmessengerever.payload.response.message.LastMessage;

/**
 * ChatDetails
 */

@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationPreview {

  private Long id;

  private String chatName;

  private Integer unreadMessagesCount;

  private ConversationType conversationType;

  private LastMessage lastMessage;
}

