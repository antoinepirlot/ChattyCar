package be.vinci.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private int id;
    private Position origin;
    private Position destination;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departure;
    @JsonProperty("driver_id")
    private Integer driverId;
    @JsonProperty("available_seating")
    private int availableSeating;
}