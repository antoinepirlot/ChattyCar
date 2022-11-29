package be.vinci.chattycar.positions;

import org.springframework.stereotype.Service;

@Service
public class PositionsService {
  private static final long EARTH_RADIUS = 6378137;

  public PositionsService() {

  }

  /**
   * Calculate the distance between 2 points on Earth.
   * @param startLon the longitude of the first point
   * @param endLong the longitude of the second point
   * @param startLat the latitude of the first point
   * @param endLat the latitude of the second point
   * @return the distance between the first and the second point into Meters
   */
  public int calculate(double startLon, double endLong, double startLat, double endLat) {
    startLon = Math.toRadians(startLon);
    endLong = Math.toRadians(endLong);
    startLat = Math.toRadians(startLat);
    endLat = Math.toRadians(endLat);
    double distanceInMeters = Math.acos(
        Math.sin(startLat) * Math.sin(endLat)
            + Math.cos(startLat) * Math.cos(endLat) * Math.cos(startLon - endLong)
    ) * EARTH_RADIUS;
    return (int) Math.ceil(distanceInMeters);
  }
}
