package be.vinci.chattycar.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {
    private String email;
    private String firstname;
    private String lastname;
    private String password;

    public InsecureCredentials getInsecureCredentials() {
        InsecureCredentials insecureCredentials = new InsecureCredentials();
        insecureCredentials.setEmail(email);
        insecureCredentials.setPassword(password);
        return insecureCredentials;
    }
}
