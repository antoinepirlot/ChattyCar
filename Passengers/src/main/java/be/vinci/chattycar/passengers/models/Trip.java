package be.vinci.chattycar.passengers.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
  private int id;
  private Position origin;
  private Position destination;
  @JsonProperty("departure_date")
  private String departureDate;
  @JsonProperty("driver_id")
  private int driverId;
  @JsonProperty("available_seating")
  private int availableSeating;
}
