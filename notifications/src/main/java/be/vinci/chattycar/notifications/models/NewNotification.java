package be.vinci.chattycar.notifications.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewNotification {

  private int userId;
  private int tripId;
  private String date;
  private String notificationText;

  public Notification toNotification() {
    return new Notification(0, userId, tripId, date, notificationText);
  }
}
