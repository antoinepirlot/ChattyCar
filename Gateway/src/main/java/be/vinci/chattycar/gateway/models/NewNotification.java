package be.vinci.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewNotification {

  @JsonProperty("user_id")
  private int userId;
  @JsonProperty("trip_id")
  private int tripId;
  private LocalDate date;
  @JsonProperty("notification_text")
  private String notificationText;

  public Notification toNotification() {
    return new Notification(0, userId, tripId, date, notificationText);
  }
}
