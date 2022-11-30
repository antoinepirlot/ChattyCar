package be.vinci.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
  @JsonProperty("notification_id")
  private int notificationId;
  @JsonProperty("user_id")
  private int userId;
  @JsonProperty("trip_id")
  private int tripId;
  private LocalDate date;
  @JsonProperty("notification_text")
  private String notificationText;

}
