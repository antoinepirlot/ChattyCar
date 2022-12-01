package be.vinci.chattycar.positions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PositionsController {

  private final PositionsService positionsService;

  public PositionsController(PositionsService positionsService) {
    this.positionsService = positionsService;
  }

  @GetMapping("/positions")
  public int getDistanceBetween2Points(
      @RequestParam double startLon,
      @RequestParam double endLon,
      @RequestParam double startLat,
      @RequestParam double endLat
  ) {
    if (startLon >= 90 || startLon <= -90 || endLon >= 90 || endLon <= -90
        || startLat >= 180 || startLat <= -180 || endLat >= 180 || endLat <= -180
    ) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    int distanceInMeters = this.positionsService.calculate(startLon, endLon, startLat, endLat);
    if (distanceInMeters < 0) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return distanceInMeters;
  }
}
