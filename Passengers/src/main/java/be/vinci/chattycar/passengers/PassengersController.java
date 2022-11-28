package be.vinci.chattycar.passengers;

import be.vinci.chattycar.passengers.exceptions.InscriptionExists400Exception;
import be.vinci.chattycar.passengers.exceptions.InscriptionNotFound404Exception;
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

  @GetMapping("/passengers/{trip_id}")
  public Passengers getPassengers(@PathVariable Integer trip_id) throws TripNotFound404Exception {
    return service.getPassengers(trip_id);
  }

  @DeleteMapping("/passengers/{trip_id}")
  public void deleteOne(@PathVariable Integer trip_id) throws TripNotFound404Exception {
    service.deleteOne(trip_id);
  }

  @PostMapping("/passengers/{trip_id}/user/{user_id}")
  public ResponseEntity<Void> createOne(@PathVariable Integer trip_id, @PathVariable Integer user_id) throws InscriptionExists400Exception {
    if (trip_id < 0 || user_id < 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    service.createOne(trip_id, user_id);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/passengers/{trip_id}/user/{user_id}")
  public Passenger getInscription(@PathVariable Integer trip_id, @PathVariable Integer user_id) throws TripNotFound404Exception, UserNotFound404Exception {
    return service.readOne(trip_id, user_id);
  }

  @PutMapping("/passengers/{trip_id}/user/{user_id}")
  public ResponseEntity<Void> updatePassengerStatus(@PathVariable Integer trip_id, @PathVariable Integer user_id, @RequestBody String status) throws InscriptionNotFound404Exception {
    if (status.isEmpty() || !(status.equals("accepted") || status.equals("refused"))) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    service.updateOne(trip_id, user_id, status);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/passengers/{trip_id}/user/{user_id}")
  public ResponseEntity<Object> deleteInscription(@PathVariable Integer trip_id, @PathVariable Integer user_id) throws InscriptionNotFound404Exception {
    service.deleteOne(trip_id, user_id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("passengers/user/{user_id}")
  public PassengerTrips getTrips(@PathVariable Integer user_id) throws UserNotFound404Exception {
    return service.readTripsFromPassenger(user_id);
  }

  @DeleteMapping("passengers/user/{user_id}")
  public void deleteAllInscriptions(@PathVariable Integer user_id) throws UserNotFound404Exception {
    service.deleteTripsFromPassenger(user_id);
  }

}
