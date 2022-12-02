package be.vinci.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoIdPassenger {
  @JsonProperty("trip_id")
  private Integer tripId;
  @JsonProperty("user_id")
  private Integer userId;
  private String status;
}

