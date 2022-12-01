package be.vinci.chattycar.passengers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "passenger")
public class Passenger {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @JsonProperty("trip_id")
  @Column(name = "trip_id")
  private Integer tripId;
  @JsonProperty("user_id")
  @Column(name = "user_id")
  private Integer userId;
  private String status;

  public NoIdPassenger removeId() {
    return new NoIdPassenger(tripId, userId, status);
  }
}
