package be.vinci.chattycar.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Passengers {
    private List<User> pending;
    private List<User> accepted;
    private List<User> refused;
}
