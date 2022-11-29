package be.vinci.chattycar.passengers.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PassengerTrips {
  private List<Trip> pending;
  private List<Trip> accepted;
  private List<Trip> refused;
}
