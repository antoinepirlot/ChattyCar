package be.vinci.chattycar.notifications.data;

import be.vinci.chattycar.notifications.models.Notification;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface NotificationsRepository extends CrudRepository<Notification, Integer> {

  Iterable<Notification> findByUserId(int userId);

  @Transactional
  void deleteByUserId(int userId);
}
