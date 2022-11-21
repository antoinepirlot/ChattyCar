package be.vinci.chattycar.trips.data;

import be.vinci.chattycar.trips.models.Position;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "positions")
public interface PositionsProxy {

  @GetMapping("/positions/{id}")
  Position readOne(@PathVariable int id);

}
