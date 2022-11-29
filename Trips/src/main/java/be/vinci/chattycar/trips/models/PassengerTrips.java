package be.vinci.chattycar.trips.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PassengerTrips {

  private List<Trip> pending;
  private List<Trip> accepted;
  private List<Trip> refused;
}
