package be.vinci.chattycar.notifications.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
