package ru.nloktionov.bestmessengerever.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTypingResponse {
    private Long chatId;
    private String userFirstname;
    private Boolean isTyping;
}
