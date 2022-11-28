package be.vinci.chattycar.notifications;

import be.vinci.chattycar.notifications.data.NotificationsRepository;
import be.vinci.chattycar.notifications.data.UsersProxy;
import be.vinci.chattycar.notifications.models.NewNotification;
import be.vinci.chattycar.notifications.models.Notification;
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
   * @return notification created, null if the user id or trip id don't exist
   */
  public Notification createOne(NewNotification newNotification) {
    try {
      usersProxy.readOneById(newNotification.getUserId());
    } catch (Exception e) {
      return null;
    }
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
   * @return true if notifications from the user could be deleted, false if not
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
