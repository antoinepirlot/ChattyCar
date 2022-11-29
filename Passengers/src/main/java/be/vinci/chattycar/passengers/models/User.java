package be.vinci.chattycar.passengers.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private int id;
  private String email;
  private String firstname;
  private String lastname;
}
