package be.vinci.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewTrip {
    private Position origin;
    private Position destination;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate departure;
    @JsonProperty("driver_id")
    private Integer driverId;
    @JsonProperty("available_seating")
    private Integer availableSeating;
}
