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
        Credentials credentials = new Credentials(email, password);
        return credentials;
    }
}
