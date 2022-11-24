package be.vinci.chattycar.notifications.data;

import be.vinci.chattycar.notifications.models.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationsRepository extends CrudRepository<Notification, Integer> {

  Iterable<Notification> findByUser_id(int userId);
}
