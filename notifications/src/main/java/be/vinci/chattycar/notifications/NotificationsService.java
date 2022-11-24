package be.vinci.chattycar.notifications;

import be.vinci.chattycar.notifications.data.NotificationsRepository;
import be.vinci.chattycar.notifications.models.NewNotification;
import be.vinci.chattycar.notifications.models.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationsService {

  private final NotificationsRepository repository;

  public NotificationsService(NotificationsRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a notification
   * @param newNotification Notification to create
   * @return notification created
   */
  public Notification createOne(NewNotification newNotification) {
    Notification notification = newNotification.toNotification();
    repository.save(notification);
    return notification;
  }

}
