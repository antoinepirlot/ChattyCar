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

  List<Trip> getTripsByAvailableSeatingGreaterThanAndOriginEqualsOrderByIdDesc(int availableSeating, Position origin);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDestinationEqualsOrderByIdDesc(int availableSeating, Position destination);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndOriginEqualsAndDestinationEqualsOrderByIdDesc(int availableSeating, Position origin, Position destination);

  List<Trip> getTripsByAvailableSeatingGreaterThanAndDepartureEqualsOrderByIdDesc(int availableSeating, LocalDate departureDate);

  Trip getTripsById(int id);

  Trip deleteTripById(int id);

  List<Trip> getTripsByDriverId(int driverId);
}
