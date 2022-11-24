package be.vinci.chattycar.notifications;

import be.vinci.chattycar.notifications.data.NotificationsRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationsService {

  private final NotificationsRepository repository;

  public NotificationsService(NotificationsRepository repository) {
    this.repository = repository;
  }


}
