package be.vinci.chattycar.passengers;

import be.vinci.chattycar.passengers.exceptions.PassengerExists400Exception;
import be.vinci.chattycar.passengers.exceptions.PassengerNotFound404Exception;
import be.vinci.chattycar.passengers.exceptions.TripNotFound404Exception;
import be.vinci.chattycar.passengers.exceptions.UserNotFound404Exception;
import be.vinci.chattycar.passengers.models.Passenger;
import be.vinci.chattycar.passengers.models.PassengerTrips;
import be.vinci.chattycar.passengers.models.Passengers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class PassengersController {

  private final PassengersService service;

  public PassengersController(PassengersService service) {
    this.service = service;
  }

  /**
   * Get list of passengers of a trip, with pending, accepted and refused status
   * @param tripId id of the trip
   * @return Passengers
   * @throws TripNotFound404Exception if the trip does not exist
   */
  @GetMapping("/passengers/{trip_id}")
  public Passengers getPassengers(@PathVariable("trip_id") Integer tripId) {
    return service.getPassengers(tripId);
  }

  /**
   * Delete all passengers of a trip
   * @param tripId id of the trip
   * @throws TripNotFound404Exception if the trip does not exist
   */
  @DeleteMapping("/passengers/{trip_id}")
  public void deleteOne(@PathVariable("trip_id") Integer tripId) throws TripNotFound404Exception {
    service.deleteOne(tripId);
  }

  /**
   * Add user as passenger to a trip with pending status (create passenger)
   * @param tripId id of the trip
   * @param userId id of the user
   * @return 201 if User added as pending passenger or throws an error
   * @throws PassengerExists400Exception if the passenger does not exist
   */
  @PostMapping("/passengers/{trip_id}/user/{user_id}")
  public ResponseEntity<Void> createOne(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId) throws PassengerExists400Exception {
    if (tripId < 0 || userId < 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    service.createOne(tripId, userId);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Get passenger status
   * @param tripId id of the trip
   * @param userId id of the user
   * @return the status of the passenger
   * @throws TripNotFound404Exception if the trip does not exist
   * @throws UserNotFound404Exception if the user does not exist
   */
  @GetMapping("/passengers/{trip_id}/user/{user_id}")
  public String getInscription(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId) throws TripNotFound404Exception, UserNotFound404Exception {
    return service.getPassengerStatus(tripId, userId);
  }

  /**
   * Update passenger status
   * @param tripId id of the trip
   * @param userId id of the user
   * @param status status of the passenger
   * @return 201 if the status is updated
   * @throws PassengerNotFound404Exception if the passenger does not exist
   */
  @PutMapping("/passengers/{trip_id}/user/{user_id}")
  public ResponseEntity<Void> updatePassengerStatus(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId, @RequestBody String status) throws PassengerNotFound404Exception {
    if (status.isEmpty() || !(status.equals("accepted") || status.equals("refused")))
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    service.updateOne(tripId, userId, status);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Remove user from passengers of a trip
   * @param tripId id of the trip
   * @param userId id of the user
   * @return 200 if the passenger was deleted
   * @throws PassengerNotFound404Exception if the passenger does not exist
   */
  @DeleteMapping("/passengers/{trip_id}/user/{user_id}")
  public ResponseEntity<Object> deleteInscription(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId) throws PassengerNotFound404Exception {
    service.deleteOne(tripId, userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Get all trips of a passenger
   * @param userId id of the user
   * @return  Get list of trips of a passenger, with pending, accepted and refused status
   * @throws UserNotFound404Exception if the user does not exists
   */
  @GetMapping("passengers/user/{user_id}")
  public PassengerTrips getTrips(@PathVariable("user_id") Integer userId) throws UserNotFound404Exception {
    return service.readTripsFromPassenger(userId);
  }

  /**
   * Delete all trips of a passenger
   * @param userId id of the user
   * @throws UserNotFound404Exception if the user does not exists
   */
  @DeleteMapping("passengers/user/{user_id}")
  public void deleteAllInscriptions(@PathVariable("user_id") Integer userId) throws UserNotFound404Exception {
    service.deleteTripsFromPassenger(userId);
  }

}
