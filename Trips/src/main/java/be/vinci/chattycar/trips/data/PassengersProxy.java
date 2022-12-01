package be.vinci.chattycar.trips.data;

import be.vinci.chattycar.trips.models.PassengerTrips;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "passengers")
public interface PassengersProxy {

  @GetMapping("/passengers/{trip_id}")
  PassengerTrips readPassengerTrips(@PathVariable("trip_id") int tripId);
}
