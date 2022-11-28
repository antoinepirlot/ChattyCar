package be.vinci.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InsecureCredentials {
    private String email;
    private String password;

    public Credentials toCredentials(String hashedPassword) {
        return new Credentials(email, hashedPassword);
    }
}
