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
    var toSave = newTrip.toTrip();
    Trip res = null;
    try {
      res = this.repository.save(toSave);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return res;
  }

  public List<Trip> getAll(LocalDate departureDate, double originLat, double originLon,
      double destinationLat, double destinationLon) {
    List<Trip> trips = this.repository.getTripsByOrderByIdDesc();
    List<Trip> trips1 = trips.stream().filter(t -> t.getAvailableSeating() > 0).toList();
    //TODO calcul distance avec positions qui calcule la distance
    return trips;
  }

  /**
   * Get one trip identified by its id
   * @param id the trip's id
   * @return the trip matching with the id
   */
  public Trip getOneById(int id) {
    return this.repository.getTripsById(id);
  }

  /**
   * Delete one trip
   * @param trip the trip to remove
   */
  public void deleteOne(Trip trip) {
    try {
      this.repository.delete(trip);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * get all driver's trips
   * @param id the driver's id
   * @return thr list of driver's trips
   */
  public List<Trip> getDriverTrips(int id) {
    return this.repository.getTripsByDriverId(id);
  }
}
