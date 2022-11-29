package be.vinci.chattycar.trips;

import be.vinci.chattycar.trips.models.NewTrip;
import be.vinci.chattycar.trips.models.PassengerTrips;
import be.vinci.chattycar.trips.models.Trip;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
      //TODO parse string to local date
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
      @RequestParam(required = false) double originLat,
      @RequestParam(required = false) double originLon,
      @RequestParam(required = false) double destinationLat,
      @RequestParam(required = false) double destinationLon
  ) {
    List<Trip> trips = this.service.getAll(
        departureDate, originLat, originLon, destinationLat, destinationLon
    );
    if (trips == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(trips, HttpStatus.ACCEPTED);
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
    if (createdTrip == null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
  }

  @GetMapping("/trips/{id}")
  public ResponseEntity<Trip> getOneById(@PathVariable int id) {
    Trip trip = this.service.getOneById(id);
    if (trip == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(trip, HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/trips/{id}")
  public void deleteOne(@PathVariable int id) {
    Trip trip = this.service.getOneById(id);
    if (trip == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    this.service.deleteOne(trip);
  }

  @GetMapping("/trips/{id}/driver")
  public ResponseEntity<List<Trip>> getDriverTrips(@PathVariable int id) {
    List<Trip> driverTrips = this.service.getDriverTrips(id);
    if (driverTrips == null || driverTrips.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(driverTrips, HttpStatus.ACCEPTED);
  }

  @GetMapping("/trips/{id}/passenger")
  public ResponseEntity<PassengerTrips> getFuturePassengerTrips(@PathVariable int id) {
    PassengerTrips futurePassengerTrips = this.service.getFuturePassengerTrips(id);
    if (futurePassengerTrips == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(futurePassengerTrips, HttpStatus.ACCEPTED);
  }
}
