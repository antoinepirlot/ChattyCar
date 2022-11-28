package be.vinci.chattycar.trips;

import be.vinci.chattycar.trips.data.PositionsProxy;
import be.vinci.chattycar.trips.data.TripsRepository;
import be.vinci.chattycar.trips.models.NewTrip;
import be.vinci.chattycar.trips.models.Trip;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TripsService {

  private final TripsRepository repository;
  private final PositionsProxy positionsProxy;

  public TripsService(TripsRepository repository, PositionsProxy positionsProxy) {
    this.repository = repository;
    this.positionsProxy = positionsProxy;
  }

  public Trip createOne(NewTrip newTrip) {
    if (this.repository.existsByDriverIdAndDeparture(newTrip.getDriverId(),
        newTrip.getDeparture())) {
      return null;
    }
    return this.repository.save(newTrip.toTrip());
  }

  public List<Trip> getAll(LocalDate departureDate, double originLat, double originLon,
      double destinationLat, double destinationLon) {
    //TODO filtering
    List<Trip> trips = this.repository.getTripsBy();
    return trips;
  }
}
