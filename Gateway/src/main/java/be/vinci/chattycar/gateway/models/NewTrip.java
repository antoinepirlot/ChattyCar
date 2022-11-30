package be.vinci.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewTrip {
    private Position origin;
    private Position destination;
    @DateTimeFormat(iso = ISO.DATE)
    @JsonProperty("departure_date")
    private LocalDate departure;
    @JsonProperty("driver_id")
    private int driverId;
    @JsonProperty("available_seating")
    private int availableSeating;
}
