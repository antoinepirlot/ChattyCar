package be.vinci.chattycar.passengers.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  // TODO: 28/11/2022 On génère id ? 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String email;
  private String firstname;
  private String lastname;
}
