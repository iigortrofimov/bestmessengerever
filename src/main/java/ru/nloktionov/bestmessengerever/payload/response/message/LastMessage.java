package ru.nloktionov.bestmessengerever.payload.response.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.nloktionov.bestmessengerever.enums.MessageStatus;

import java.util.Date;

@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastMessage {

    private Long id;

    private String chatId;

    private String content;

    private Date timestamp;

    private MessageStatus messageStatus;
}
