package ru.nloktionov.bestmessengerever.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetails {

  private String id;

  private String username;

  private String firstname;

  private String lastname;

}

