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

  private static final int LIST_LIMIT = 20;

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
    Trip toSave = newTrip.toTrip();
    return this.repository.save(toSave);
  }

  /**
   * Get the lis of all trips
   * @return the list of trips
   */
  public  List<Trip> getAll() {
    List<Trip> trips = this.repository.getTripsByAvailableSeatingGreaterThanOrderByIdDesc(0);
    return trips.stream().limit(LIST_LIMIT).toList();
  }

  /**
   * Get all trips matching one position
   * @param longitude the longitude of the position
   * @param latitude the latitude of the position
   * @param origin true if the position is the origin, false if the position is the destination
   * @return the list of trips matching with the origin or the destination position
   */
  public List<Trip> getAll(double longitude, double latitude, boolean origin) {
    Position position = new Position();
    position.setLongitude(longitude);
    position.setLatitude(latitude);
    List<Trip> trips;
    if (origin) {
      trips =  this.repository.getTripsByAvailableSeatingGreaterThanAndOriginEqualsOrderByIdDesc(0, position);
    } else {
      trips = this.repository.getTripsByAvailableSeatingGreaterThanAndDestinationEqualsOrderByIdDesc(0, position);
    }
    return trips.stream().limit(LIST_LIMIT).toList();
  }

  public List<Trip> getAll(double originLon, double originLat, double destinationLon, double destinationLat) {
    Position origin = new Position();
    Position destination = new Position();
    origin.setLongitude(originLon);
    origin.setLatitude(originLat);
    destination.setLongitude(destinationLon);
    destination.setLatitude(destinationLat);
    List<Trip> trips = this.repository.getTripsByAvailableSeatingGreaterThanAndOriginEqualsAndDestinationEqualsOrderByIdDesc(0, origin, destination);
    trips = trips.stream().limit(LIST_LIMIT).toList();
    //TODO sort by distance
    return trips;
  }

  /**
   * Get all trips at the departure date
   * @param departureDate the departure date
   * @return the list of trips matching departure date
   */
  public List<Trip> getAll(LocalDate departureDate) {
    List<Trip> trips = this.repository.getTripsByAvailableSeatingGreaterThanAndDepartureEqualsOrderByIdDesc(0, departureDate);
    trips = trips.stream().limit(LIST_LIMIT).toList();
    return trips;
  }

  /**
   * Get all trips at the departure date with specified position
   * @param departureDate the departure date
   * @param longitude the destination longitude
   * @param latitude the destination latitude
   * @param origin true if position is origin,false if the position is destination
   * @return the list of trips matching params
   */
  public List<Trip> getAll(LocalDate departureDate, double longitude, double latitude, boolean origin) {
    Position position = new Position();
    position.setLongitude(longitude);
    position.setLatitude(latitude);
    List<Trip> trips;
    if (origin) {
      trips = this.repository.getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndOriginEqualsOrderByIdDesc(0, departureDate, position);
    } else {
      trips = this.repository.getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndDestinationEqualsOrderByIdDesc(
          0, departureDate, position);
    }
    return trips.stream().limit(LIST_LIMIT).toList();
  }

  public List<Trip> getAll(LocalDate departureDate, double originLon, double originLat, double destinationLon, double destinationLat) {
    Position origin = new Position();
    Position destination = new Position();
    origin.setLongitude(originLon);
    origin.setLatitude(originLat);
    destination.setLongitude(destinationLon);
    destination.setLatitude(destinationLat);
    List<Trip> trips = this.repository.getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndOriginEqualsAndDestinationEqualsOrderByIdDesc(0, departureDate, origin, destination);
    trips = trips.stream().limit(LIST_LIMIT).toList();
    //TODO sort by distance
    int distance = this.getDistance(origin, destination);
    System.out.println(distance);
    return trips;
  }

  /**
   * Get distance from Positions service
   * @param origin the origin
   * @param destination the destination
   * @return the distance between origin and destination
   */
  private int getDistance(Position origin, Position destination) {
    return this.positionsProxy.getDistance(
        origin.getLongitude(),
        destination.getLongitude(),
        origin.getLatitude(),
        destination.getLatitude()
    );
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
    this.repository.delete(trip);
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
