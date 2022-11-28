package be.vinci.chattycar.notifications;

import be.vinci.chattycar.notifications.data.NotificationsRepository;
import be.vinci.chattycar.notifications.data.UsersProxy;
import be.vinci.chattycar.notifications.models.NewNotification;
import be.vinci.chattycar.notifications.models.Notification;
import be.vinci.chattycar.notifications.models.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationsService {

  private final NotificationsRepository repository;
  private final UsersProxy usersProxy;

  public NotificationsService(NotificationsRepository repository, UsersProxy proxy) {
    this.repository = repository;
    this.usersProxy = proxy;
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

  /**
   * Reads all notifications from a user
   * @param userId Id of the user
   * @return The list of notifications from this user
  */
  public Iterable<Notification> readNotificationsFromUserId(int userId) {
    try {
      usersProxy.readOneById(userId);
    } catch (Exception e) {
      return null;
    }
    return repository.findByUserId(userId);
  }

  /**
   * Deletes all notifications from a user
   * @param userId Id of the user
   */
  public boolean deleteNotificationsFromUserId(int userId) {
    try {
      usersProxy.readOneById(userId);
    } catch (Exception e) {
      return false;
    }
    repository.deleteByUserId(userId);
    return true;
  }

}
