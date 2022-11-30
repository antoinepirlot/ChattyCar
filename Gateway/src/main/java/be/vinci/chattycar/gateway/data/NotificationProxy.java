package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.NewNotification;
import be.vinci.chattycar.gateway.models.Notification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "notifications")
public interface NotificationProxy {

  @PostMapping("/notifications")
  ResponseEntity<Notification> createOne(@RequestBody NewNotification newNotification);

  @GetMapping("/notifications/user/{id}")
  Iterable<Notification> readFromUser(@PathVariable int id);

  @DeleteMapping("/notifications/user/{id}")
  void deleteFromUser(@PathVariable int id);
}
