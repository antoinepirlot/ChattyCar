package be.vinci.chattycar.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewTrip {
    private Position origin;
    private Position destination;
    private String departure_date;
    private int driver_id;
    private int available_seating;
}
