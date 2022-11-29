package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.Notification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "notification")
public interface NotificationProxy {

  @GetMapping("/notifications/users/{id}")
  Iterable<Notification> readFromUser(@PathVariable int id);

  @DeleteMapping("/notifications/users/{id}")
  void deleteFromUser(@PathVariable int id);
}
