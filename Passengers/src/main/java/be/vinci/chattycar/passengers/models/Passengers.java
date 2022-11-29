package be.vinci.chattycar.passengers.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Getter
public class Passengers {
    private List<User> pending;
    private List<User> accepted;
    private List<User> refused;
}
