package be.vinci.chattycar.trips.data;

import be.vinci.chattycar.trips.models.Trip;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripsRepository extends CrudRepository<Trip, Long> {
  boolean existsByDriverIdAndDeparture(int driverId, LocalDate departure);
  List<Trip> getTripsByOrderByIdDesc();
}
