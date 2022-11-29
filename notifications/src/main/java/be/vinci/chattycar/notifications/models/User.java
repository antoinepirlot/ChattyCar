package be.vinci.chattycar.notifications.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
  private String email;
  private String firstname;
  private String lastname;

}