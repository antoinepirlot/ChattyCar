package be.vinci.chattycar.passengers.data;

import be.vinci.chattycar.passengers.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {
  @GetMapping("/users/{id}")
  User readOne(@PathVariable Integer id);
}
