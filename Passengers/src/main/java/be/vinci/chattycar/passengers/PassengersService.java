package be.vinci.chattycar.passengers;

import be.vinci.chattycar.passengers.data.PassengersRepository;
import be.vinci.chattycar.passengers.data.TripsProxy;
import be.vinci.chattycar.passengers.data.UsersProxy;
import be.vinci.chattycar.passengers.exceptions.InscriptionExists400Exception;
import be.vinci.chattycar.passengers.exceptions.InscriptionNotFound404Exception;
import be.vinci.chattycar.passengers.exceptions.InscriptionNotValidException;
import be.vinci.chattycar.passengers.exceptions.NoSeatLeft400Exception;
import be.vinci.chattycar.passengers.exceptions.TripNotFound404Exception;
import be.vinci.chattycar.passengers.exceptions.TripOrUserNotFound404Exception;
import be.vinci.chattycar.passengers.exceptions.UserNotFound404Exception;
import be.vinci.chattycar.passengers.models.Passenger;
import be.vinci.chattycar.passengers.models.PassengerTrips;
import be.vinci.chattycar.passengers.models.Passengers;
import be.vinci.chattycar.passengers.models.Trip;
import java.util.Optional;

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
   * @throws TripNotFound404Exception
   */
  public Passengers getPassengers(Integer tripId) throws TripNotFound404Exception {
    if (!repository.existsById(tripId)) throw new TripNotFound404Exception();
    return repository.findAllPassengersByTripId(tripId);
  }

  /**
   * Deletes all passengers of a trip
   * @param tripId id of the trip
   * @return True if the passenger of a trip were deleted, false if it couldn't be found
   */
  public boolean deleteOne(Integer tripId) throws TripNotFound404Exception {
    if (!repository.existsById(tripId)) throw new TripNotFound404Exception();
    repository.deleteByTripId(tripId);
    return true;
  }

  /**
   * Creates a passenger for a trip
   * @param tripId id of the trip
   * @param userId id of the user
   * @return The passenger created with its id, or null if it already existed
   */
  public Passenger createOne(Integer tripId, Integer userId) throws TripNotFound404Exception,
      UserNotFound404Exception, NoSeatLeft400Exception, InscriptionExists400Exception, InternalError {
    if (repository.existsByIds(tripId, userId)) throw new InscriptionExists400Exception();
    if(!repository.userExists(userId)) throw new UserNotFound404Exception();
    if(!repository.tripExists(tripId)) throw new TripNotFound404Exception();
    if(repository.getTrip(tripId).getAvailableSeating() < 1) throw new NoSeatLeft400Exception();
    // TODO: 28/11/2022 Comment créer un trip ? 
    //return repository.save(new Passenger(tripId, userId, "pending"));
    return null;
  }

  /**
   * Reads a passenger in a trip
   * @param tripId id of the trip
   * @param userId id of the user
   * @return The passenger, or null if it couldn't be found
   */
  public Passenger readOne(long tripId, long userId) {
    Optional<Passenger> p = repository.findByIds(tripId, userId);
    if (!p.isPresent()) throw new TripOrUserNotFound404Exception();
    return p.get();
  }

  /**
   * Updates an inscription
   * @param tripId id of the trip
   * @param userId id of the user
   * @return True if the inscription was updated, or null if it couldn't be found
   */
  public boolean updateOne(Integer tripId, Integer userId, String status)
      throws InscriptionNotValidException, UserNotFound404Exception, TripNotFound404Exception{
    if(!repository.userExists(userId)) throw new UserNotFound404Exception();
    if(!repository.tripExists(tripId)) throw new TripNotFound404Exception();
    if(!repository.existsByIds(tripId, userId)
        || !repository.findByIds(tripId, userId).get().getStatus().equals("pending")
        || !(status.equals("accepted") || status.equals("refused"))) throw new InscriptionNotValidException();
    // TODO: 28/11/2022 Comment créer un new passenger
    //repository.save(new Passenger(tripId, userId, "pending"));
    return true;
  }

  /**
   * Deletes a review from repository
   * @param tripId id of the trip
   * @param userId id of the user
   * @return True if the review was deleted, false if it couldn't be found
   */
  public boolean deleteOne(Integer tripId, Integer userId)
      throws InscriptionNotFound404Exception, UserNotFound404Exception, TripNotFound404Exception {
    if(!repository.userExists(userId)) throw new UserNotFound404Exception();
    if(!repository.tripExists(tripId)) throw new TripNotFound404Exception();
    if(!repository.existsByIds(tripId, userId)) throw new InscriptionNotFound404Exception();
    repository.deleteByIds(tripId, userId);
    return true;
  }

  /**
   * Reads all reviews from a user
   * @param userId id of the user
   * @return The list of trips from this user
   */
  public PassengerTrips readTripsFromPassenger(Integer userId) {
    return repository.findAllTripsByUserId(userId);
  }

  /**
   * Deletes all trips from a user
   * @param userId id of the user
   */
  public void deleteTripsFromPassenger(Integer userId) {
    repository.deleteByUserId(userId);
  }



}
