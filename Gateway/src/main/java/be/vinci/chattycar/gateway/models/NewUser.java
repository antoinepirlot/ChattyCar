package be.vinci.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewUser {
    private String email;
    private String firstname;
    private String lastname;
    private String password;

    public Credentials getCredentials() {
        return new Credentials(email, password);
    }
}
