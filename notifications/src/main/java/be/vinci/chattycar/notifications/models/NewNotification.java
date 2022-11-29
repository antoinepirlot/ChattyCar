package be.vinci.chattycar.notifications.models;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@ToString
public class NewNotification {

  private int userId;
  private int tripId;
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate date;
  private String notificationText;

  public Notification toNotification() {
    return new Notification(0, userId, tripId, date, notificationText);
  }
}
