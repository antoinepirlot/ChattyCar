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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Embedded
  private Position origin;
  @Embedded
  private Position destination;
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate departure;
  private int driverId;
  private int availableSeating;

}
