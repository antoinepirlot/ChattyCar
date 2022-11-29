package be.vinci.chattycar.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewNotification {

  private int userId;
  private int tripId;
  private String date;
  private String notificationText;

  public Notification toNotification() {
    return new Notification(0, userId, tripId, date, notificationText);
  }
}
