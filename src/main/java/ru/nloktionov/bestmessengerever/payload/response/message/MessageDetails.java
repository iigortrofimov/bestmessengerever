package ru.nloktionov.bestmessengerever.payload.response.message;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;
import ru.nloktionov.bestmessengerever.enums.MessageType;
import ru.nloktionov.bestmessengerever.payload.response.UserDetails;

import java.util.Date;

@Getter
@Setter
@Data
public class MessageDetails {

    private Long id;

    private MessageType messageType;

    private Long chatId;

    private String content;

    private Date timestamp;

    private MessageStatus messageStatus;

    private UserDetails sender;

    private MessageDetails parentMessage;
}
