package be.vinci.chattycar.trips;

import be.vinci.chattycar.trips.models.NewTrip;
import be.vinci.chattycar.trips.models.Trip;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TripsController {

  private final TripsService service;

  public TripsController(TripsService service) {
    this.service = service;
  }

  @PostMapping("/trips")
  public ResponseEntity<Trip> createOne(@RequestBody NewTrip newTrip) {
    if (newTrip.getDeparture() == null
        || newTrip.getOrigin() == null
        || newTrip.getDestination() == null || newTrip.getDriverId() < 1
        || newTrip.getAvailableSeating() < 1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Trip createdTrip = this.service.createOne(newTrip);
    if(createdTrip == null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
  }
}
