package be.vinci.chattycar.passengers.data;

import be.vinci.chattycar.passengers.models.Trip;
import be.vinci.chattycar.passengers.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Repository
@FeignClient(name = "trips")
public interface TripsProxy {
  @GetMapping("/trips/{id}")
  Trip readOne(@PathVariable long id);
}
