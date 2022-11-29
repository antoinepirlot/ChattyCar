package be.vinci.chattycar.passengers.models;


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
  private String departure_date;
  private int driver_id;
  private int available_seating;
}
