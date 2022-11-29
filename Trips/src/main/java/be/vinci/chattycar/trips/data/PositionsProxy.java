package be.vinci.chattycar.trips.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "positions")
public interface PositionsProxy {

  @GetMapping("/positions")
  int getDistance(@RequestParam double startLon, @RequestParam double endLon, @RequestParam double startLat, @RequestParam double endLat);

}
