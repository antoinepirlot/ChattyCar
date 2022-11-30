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
   *
   * @param newNotification Notification to create
   * @return notification created
   */
  public Notification createOne(NewNotification newNotification) {
    Notification notification = newNotification.toNotification();
    repository.save(notification);
    return notification;
  }

  /**
   * Reads all notifications from a user
   *
   * @param userId Id of the user
   * @return The list of notifications from this user
   */
  public Iterable<Notification> readNotificationsFromUserId(int userId) {
    return repository.findByUserId(userId);
  }

  /**
   * Deletes all notifications from a user
   *
   * @param userId Id of the user
   */
  public void deleteNotificationsFromUserId(int userId) {
    repository.deleteByUserId(userId);
  }

}
