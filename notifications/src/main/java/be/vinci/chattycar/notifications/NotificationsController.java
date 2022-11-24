package be.vinci.chattycar.notifications;

import be.vinci.chattycar.notifications.models.NewNotification;
import be.vinci.chattycar.notifications.models.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class NotificationsController {

  private final NotificationsService service;

  public NotificationsController(NotificationsService service) {
    this.service = service;
  }

  @PostMapping("/notifications")
  public ResponseEntity<Notification> createOne(@RequestBody NewNotification newNotification) {
    if (newNotification.getDate() == null || newNotification.getDate().equals("") ||
        newNotification.getNotification_text() == null || newNotification.getNotification_text().equals("") ||
        newNotification.getTrip_id()<1 || newNotification.getUser_id()<1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(service.createOne(newNotification), HttpStatus.CREATED);
  }
}
