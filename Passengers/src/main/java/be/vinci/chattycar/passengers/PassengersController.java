package be.vinci.chattycar.passengers;

import be.vinci.chattycar.passengers.exceptions.PassengerExists409Exception;
import be.vinci.chattycar.passengers.exceptions.PassengerNotFound404Exception;
import be.vinci.chattycar.passengers.exceptions.TripNotFound404Exception;
import be.vinci.chattycar.passengers.exceptions.UserNotFound404Exception;
import be.vinci.chattycar.passengers.models.NoIdPassenger;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PassengersController {

  private final PassengersService service;

  public PassengersController(PassengersService service) {
    this.service = service;
  }

  /**
   * Get list of passengers of a trip, with pending, accepted and refused status
   * @param tripId id of the trip
   * @return Passengers
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
    if(!service.deleteOne(tripId)) throw new TripNotFound404Exception();
    else throw new ResponseStatusException(HttpStatus.OK);
  }

  /**
   * Add user as passenger to a trip with pending status (create passenger)
   * @param tripId id of the trip
   * @param userId id of the user
   * @return 201 if User added as pending passenger or throws an error
   * @throws PassengerExists409Exception if the passenger does not exist
   */
  @PostMapping("/passengers/{trip_id}/user/{user_id}")
  public ResponseEntity<NoIdPassenger> createOne(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId) throws PassengerExists409Exception {
    if (tripId < 0 || userId < 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    Passenger newPassenger = service.createOne(tripId, userId);
    if(newPassenger == null) throw new PassengerExists409Exception();
    return new ResponseEntity<>(newPassenger.removeId(), HttpStatus.CREATED);
  }

  /**
   * Get passenger status
   * @param tripId id of the trip
   * @param userId id of the user
   * @return the status of the passenger
   * @throws PassengerNotFound404Exception the passenger is not present
   */
  @GetMapping("/passengers/{trip_id}/user/{user_id}")
  public String getPassengerStatus(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId) throws PassengerNotFound404Exception {
    String status = service.getPassengerStatus(tripId, userId);
    if(status == null) throw new PassengerNotFound404Exception();
    return status;
  }

  /**
   * Update passenger status
   * @param tripId id of the trip
   * @param userId id of the user
   * @param passengerStatusUpdate status of the passenger
   */
  @PutMapping("/passengers/{trip_id}/user/{user_id}")
  public void updatePassengerStatus(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId, @RequestParam(required = true) String passengerStatusUpdate) {
    System.out.println(passengerStatusUpdate);
    if (passengerStatusUpdate == null || (!passengerStatusUpdate.equals("accepted") && !passengerStatusUpdate.equals("refused"))
        || !service.updateOne(tripId, userId, passengerStatusUpdate)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
     throw new ResponseStatusException(HttpStatus.CREATED);
  }

  /**
   * Remove user from passengers of a trip
   * @param tripId id of the trip
   * @param userId id of the user
   * @throws PassengerNotFound404Exception if the passenger does not exist
   */
  @DeleteMapping("/passengers/{trip_id}/user/{user_id}")
  public void deleteInscription(@PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId) throws PassengerNotFound404Exception {
    if(!service.deleteOne(tripId, userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    else throw new ResponseStatusException(HttpStatus.OK);
  }

  /**
   * Get all trips of a passenger
   * @param userId id of the user
   * @return  Get list of trips of a passenger, with pending, accepted and refused status
   * @throws UserNotFound404Exception if the user does not exists
   */
  @GetMapping("passengers/user/{user_id}")
  public PassengerTrips getTrips(@PathVariable("user_id") Integer userId) throws PassengerNotFound404Exception {
    return service.getTrips(userId);
  }

  /**
   * Delete all trips of a passenger
   * @param userId id of the user
   * @throws UserNotFound404Exception if the user does not exists
   */
  @DeleteMapping("passengers/user/{user_id}")
  public void deleteTripsFromPassenger(@PathVariable("user_id") Integer userId) throws PassengerNotFound404Exception {
    if(!service.deleteTripsFromPassenger(userId)) throw new PassengerNotFound404Exception();
    else throw new ResponseStatusException(HttpStatus.OK);

  }

}
