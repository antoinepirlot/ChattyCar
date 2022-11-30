package be.vinci.chattycar.trips;

import be.vinci.chattycar.trips.data.PassengersProxy;
import be.vinci.chattycar.trips.data.PositionsProxy;
import be.vinci.chattycar.trips.data.TripsRepository;
import be.vinci.chattycar.trips.models.NewTrip;
import be.vinci.chattycar.trips.models.PassengerTrips;
import be.vinci.chattycar.trips.models.Position;
import be.vinci.chattycar.trips.models.Trip;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TripsService {

  private final TripsRepository repository;
  private final PositionsProxy positionsProxy;
  private final PassengersProxy passengersProxy;

  public TripsService(TripsRepository repository, PositionsProxy positionsProxy, PassengersProxy passengersProxy) {
    this.repository = repository;
    this.positionsProxy = positionsProxy;
    this.passengersProxy = passengersProxy;
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

  /**
   * Get the lis of all trips
   * @return the list of trips
   */
  public  List<Trip> getAll() {
    return this.repository.getTripsByAvailableSeatingGreaterThanOrderByIdDesc(0);
  }

  public List<Trip> getAll(LocalDate departureDate, Double originLat, Double originLon,
      Double destinationLat, Double destinationLon) {
    List<Trip> trips = null;
    Position origin = new Position();
    Position destination = new Position();
    origin.setLongitude(originLon);
    origin.setLatitude(originLat);
    destination.setLongitude(destinationLon);
    destination.setLatitude(destinationLat);
    if (departureDate == null) {
      if (originLat == null && originLon == null) { // No departure date & No origin
        if (destinationLat == null && destinationLon == null) { // No departure date & No origin & No destination
          trips = this.repository.getTripsByAvailableSeatingGreaterThanOrderByIdDesc(0);
          return trips.stream().filter(t -> t.getDeparture().isAfter(LocalDate.now())).toList();
        }
        if(destinationLat != null && destinationLon != null) { // No departure date & No oigin & Destination
          trips = this.repository.getTripsByAvailableSeatingGreaterThanAndDestinationEqualsOrderByIdDesc(0, destination);
        }
      }
      if (originLon != null && originLat != null) { // No departure date & Origin
        if (destinationLat == null && destinationLon == null) { // No departure date & Origin & No destination
          trips = this.repository.getTripsByAvailableSeatingGreaterThanAndOriginEqualsOrderByIdDesc(0, origin);
          return trips.stream().filter(t -> t.getDeparture().isAfter(LocalDate.now())).toList();
        }
        if(destinationLat != null && destinationLon != null) { // No departure date & Origin & Destination
          trips = this.repository.getTripsByAvailableSeatingGreaterThanAndOriginEqualsAndDestinationEqualsOrderByIdDesc(0, origin, destination);
        }
      }
    }
    //TODO calcul distance avec positions qui calcule la distance
    return trips;
  }

  private List<Trip> filterTrips(List<Trip> trips, Position position) {
    return trips.stream()
        .filter(t -> t.getOrigin().equals(position))
        .sorted((t1, t2) -> {
          double t1StartLon = t1.getOrigin().getLongitude();
          double t1StartLat = t1.getOrigin().getLatitude();
          double t1EndLon = t1.getOrigin().getLongitude();
          double t1EndLat = t1.getOrigin().getLatitude();

          double t2StartLon = t2.getOrigin().getLongitude();
          double t2StartLat = t2.getOrigin().getLatitude();
          double t2EndLon = t2.getOrigin().getLongitude();
          double t2EndLat = t2.getOrigin().getLatitude();

          int dist1 = this.positionsProxy.getDistance(t1StartLon, t1EndLon, t1StartLat, t1EndLat);
          int dist2 = this.positionsProxy.getDistance(t2StartLon, t2EndLon, t2StartLat, t2EndLat);

          return dist1 - dist2;
        })
        .toList();
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

  /**
   * Get all future passenger's trips
   * @param id the passengerId
   * @return the list of future passenger's trips
   */
  public PassengerTrips getFuturePassengerTrips(int id) {
    PassengerTrips passengerTrips = this.passengersProxy.readPassengerTrips(id);
    if (passengerTrips == null) {
      return null;
    }
    passengerTrips.setAccepted(passengerTrips.getAccepted().stream()
        .filter(p -> p.getDeparture().isAfter(LocalDate.now()))
        .toList());
    passengerTrips.setRefused(passengerTrips.getRefused().stream()
        .filter(p -> p.getDeparture().isAfter(LocalDate.now()))
        .toList());
    passengerTrips.setPending(passengerTrips.getPending().stream()
        .filter(p -> p.getDeparture().isAfter(LocalDate.now()))
        .toList());
    return passengerTrips;
  }
}
