package be.vinci.chattycar.trips;

import be.vinci.chattycar.trips.models.NewTrip;
import be.vinci.chattycar.trips.models.Trip;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TripsController {

  private final TripsService service;

  public TripsController(TripsService service) {
    this.service = service;
  }

  @GetMapping("/trips")
  public ResponseEntity<List<Trip>> getAll(
      @RequestParam(required = false) LocalDate departureDate,
      @RequestParam(required = false) double originLat,
      @RequestParam(required = false) double originLon,
      @RequestParam(required = false) double destinationLat,
      @RequestParam(required = false) double destinationLon
  ) {
    return new ResponseEntity<>(new ArrayList<Trip>(), HttpStatus.ACCEPTED);
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
