package be.vinci.chattycar.passengers;

import be.vinci.chattycar.passengers.data.PassengersRepository;
import be.vinci.chattycar.passengers.data.TripsProxy;
import be.vinci.chattycar.passengers.data.UsersProxy;
import be.vinci.chattycar.passengers.exceptions.UserNotFound404Exception;
import be.vinci.chattycar.passengers.models.Passenger;
import be.vinci.chattycar.passengers.models.PassengerTrips;
import be.vinci.chattycar.passengers.models.Passengers;
import be.vinci.chattycar.passengers.models.Trip;
import be.vinci.chattycar.passengers.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PassengersService {
  private final PassengersRepository repository;
  private final TripsProxy tripsProxy;
  private final UsersProxy usersProxy;

  public PassengersService(PassengersRepository repository, TripsProxy tripsProxy, UsersProxy usersProxy) {
    this.repository = repository;
    this.tripsProxy = tripsProxy;
    this.usersProxy = usersProxy;
  }


  /**
   * Get list of passengers of a trip, with pending, accepted and refused status
   * @param tripId id of the trip
   * @return The list of passengers of this trip
   */
  public Passengers getPassengers(Integer tripId) {
    List<Passenger> passengerList = repository.getAllPassengersByTripId(tripId);
    if(passengerList == null || passengerList.isEmpty()) return new Passengers(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    List<User> accepted = passengerList.stream()
        .filter(passenger -> passenger.getStatus().equals("accepted"))
        .map(passenger -> usersProxy.readOne(passenger.getId())).toList();

    List<User> pending = passengerList.stream()
        .filter(passenger -> passenger.getStatus().equals("pending"))
        .map(passenger -> usersProxy.readOne(passenger.getId())).toList();

    List<User> refused = passengerList.stream()
        .filter(passenger -> passenger.getStatus().equals("refused"))
        .map(passenger -> usersProxy.readOne(passenger.getId())).toList();

    return new Passengers(pending, accepted, refused);

  }

  /**
   * Deletes all passengers of a trip
   * @param tripId id of the trip
   * @return True if the passenger of a trip were deleted, false if it couldn't be found
   */
  @Transactional
  public boolean deleteOne(Integer tripId) {
    if (!repository.existsByTripId(tripId)) return false;
    repository.deleteByTripId(tripId);
    return true;
  }

  /**
   * Creates a passenger for a trip
   * @param tripId id of the trip
   * @param userId id of the user
   * @return The passenger created with its id, or null if it already existed
   */
  @Transactional
  public Passenger createOne(Integer tripId, Integer userId) {
    if (repository.existsByUserIdAndTripId(tripId, userId)) return null;
    return repository.save(new Passenger(-1, tripId, userId, "pending"));
  }

  /**
   * Get a passenger status in a trip
   * @param tripId id of the trip
   * @param userId id of the user
   * @return The passenger status or null if the passenger is not present
   */
  public String getPassengerStatus(Integer tripId, Integer userId) {
    Optional<Passenger> p = repository.findByUserIdAndTripId(tripId, userId);
    System.out.println(p);
    if (p.isEmpty()) return null;
    return p.get().getStatus();
  }

  /**
   * Updates an inscription
   * @param tripId id of the trip
   * @param userId id of the user
   * @return True if the inscription was updated, or null if it couldn't be found
   */
  @Transactional
  public boolean updateOne(Integer tripId, Integer userId, String status) {
    Optional<Passenger> optionalPassenger = repository.findByUserIdAndTripId(tripId, userId);

    System.out.println(optionalPassenger);
    if (optionalPassenger.isPresent()) {
      Passenger passenger = optionalPassenger.get();
      if (passenger.getStatus().equals("pending")) {
          passenger.setStatus(status);
          return true;
      }
    }
    return false;
  }

  /**
   * Deletes a passenger from repository
   * @param tripId id of the trip
   * @param userId id of the user
   * @return True if the review was deleted, false if it couldn't be found
   */
  @Transactional
  public boolean deleteOne(Integer tripId, Integer userId) {
    if(!repository.existsByUserIdAndTripId(tripId, userId)) return false;
    repository.deleteByUserIdAndTripId(tripId, userId);
    return true;
  }

  /**
   * Reads all trips from a passenger
   * @param userId id of the user
   * @return The list of trips from this user
   */
  public PassengerTrips getTrips(Integer userId) {
    List<Passenger> passengerList =  repository.getAllPassengersByUserId(userId);
    System.out.println(passengerList);
    if(passengerList == null || passengerList.isEmpty()) return new PassengerTrips(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    List<Trip> accepted = passengerList.stream()
        .filter(passenger -> passenger.getStatus().equals("accepted"))
        .map(passenger -> tripsProxy.readOne(passenger.getId())).toList();

    List<Trip> pending = passengerList.stream()
        .filter(passenger -> passenger.getStatus().equals("pending"))
        .map(passenger -> tripsProxy.readOne(passenger.getId())).toList();

    List<Trip> refused = passengerList.stream()
        .filter(passenger -> passenger.getStatus().equals("refused"))
        .map(passenger -> tripsProxy.readOne(passenger.getId())).toList();

    return new PassengerTrips(pending, accepted, refused);
  }

  /**
   * Deletes all trips from a user
   * @param userId id of the user
   */
  @Transactional
  public boolean deleteTripsFromPassenger(Integer userId) throws UserNotFound404Exception {
    if(!repository.existsByUserId(userId)) return false;
    repository.deleteByUserId(userId);
    return true;
  }



}
