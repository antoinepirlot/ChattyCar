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

    public InsecureCredentials getInsecureCredentials() {
        InsecureCredentials insecureCredentials = new InsecureCredentials();
        insecureCredentials.setEmail(email);
        insecureCredentials.setPassword(password);
        System.out.println("insecure credentials");
        System.out.println(insecureCredentials);
        return insecureCredentials;
    }
}
