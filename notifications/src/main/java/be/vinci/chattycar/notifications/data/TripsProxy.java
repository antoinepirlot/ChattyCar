package be.vinci.chattycar.notifications.data;

import be.vinci.chattycar.notifications.models.Trip;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "trips")
public interface TripsProxy {

  @GetMapping("/trips/{id}")
  Trip getOneById(@PathVariable int id);

}
