package be.vinci.chattycar.trips.data;

import be.vinci.chattycar.trips.models.Position;
import be.vinci.chattycar.trips.models.Trip;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripsRepository extends CrudRepository<Trip, Long> {
  boolean existsByDriverIdAndDeparture(int driverId, LocalDate departure);
  List<Trip> getTripsByAvailableSeatingGreaterThanOrderByIdDesc(int availableSeating);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndOriginEqualsOrderByOrigin(int availableSeating, Position origin);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDestinationEqualsOrderByDestination(int availableSeating, Position destination);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndOriginEqualsAndDestinationEqualsOrderByIdDesc(int availableSeating, Position origin, Position destination);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsOrderByIdDesc(int availableSeating, LocalDate departureDate);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndOriginEqualsOrderByOrigin(int availableSeating, LocalDate departureDate, Position origin);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndDestinationEqualsOrderByDestination(int availableSeating, LocalDate departureDate, Position destination);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndOriginEqualsAndDestinationEqualsOrderByIdDesc(int availableSeating, LocalDate departureDate, Position origin, Position destination);

  Trip getTripsById(int id);
  List<Trip> getTripsByDriverId(int driverId);
}
