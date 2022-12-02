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

  List<Trip> getTripsByAvailableSeatingGreaterThanAndOriginEquals(int availableSeating, Position origin);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDestinationEquals(int availableSeating, Position destination);

  List<Trip> getTripsByAvailableSeatingGreaterThan(int availableSeating);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsOrderByIdDesc(int availableSeating, LocalDate departureDate);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndOriginEquals(int availableSeating, LocalDate departureDate, Position origin);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsAndDestinationEquals(int availableSeating, LocalDate departureDate, Position destination);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEquals(int availableSeating, LocalDate departureDate);

  Trip getTripsById(int id);
  List<Trip> getTripsByDriverId(int driverId);
}
