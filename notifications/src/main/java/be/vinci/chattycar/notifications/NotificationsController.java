package be.vinci.chattycar.notifications;

import be.vinci.chattycar.notifications.models.NewNotification;
import be.vinci.chattycar.notifications.models.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        newNotification.getNotificationText() == null || newNotification.getNotificationText().equals("") ||
        newNotification.getTripId()<1 || newNotification.getUserId()<1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(service.createOne(newNotification), HttpStatus.CREATED);
  }

  @GetMapping("/notifications/users/{id}")
  public Iterable<Notification> readFromUser(@PathVariable int id) {
    Iterable<Notification> notifications = service.readNotificationsFromUserId(id);
    if(notifications == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return notifications;
  }

  @DeleteMapping("/notifications/users/{id}")
  public void deleteFromUser(@PathVariable int id) {
    if(!service.deleteNotificationsFromUserId(id))
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }
}
