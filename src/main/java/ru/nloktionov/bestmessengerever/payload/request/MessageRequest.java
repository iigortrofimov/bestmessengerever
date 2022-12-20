package ru.nloktionov.bestmessengerever.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private Long chatId;

    private Long recipientId;

    private Long parentMessageId;

    private String content;

    public MessageRequest(Long chatId, String content) {
        this.chatId = chatId;
        this.content = content;
    }
}
