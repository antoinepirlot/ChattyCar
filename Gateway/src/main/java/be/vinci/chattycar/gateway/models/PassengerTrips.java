package be.vinci.chattycar.gateway.models;

import lombok.*;

import java.util.List;

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
