package be.vinci.chattycar.trips.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewTrip {
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "latitude", column = @Column(name = "origin_latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "origin_longitude")),
  })
  private Position origin;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "latitude", column = @Column(name = "destination_latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "destination_longitude")),
  })
  private Position destination;
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate departure;
  @JsonProperty("driver_id")
  private int driverId;
  @JsonProperty("available_seating")
  private int availableSeating;

  public Trip toTrip() {
    return new Trip(0, origin, destination, departure, driverId, availableSeating);
  }
}
