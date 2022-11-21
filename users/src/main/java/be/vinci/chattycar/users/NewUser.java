package be.vinci.chattycar.users;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewUser {
  private String email;
  private String firstname;
  private String lastname;

  public User toUser() {
    return new User(0, email, firstname, lastname);
  }
}