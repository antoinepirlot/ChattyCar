package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.NewUser;
import be.vinci.chattycar.gateway.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {
    @PostMapping("/users")
    ResponseEntity<User> createOne(@RequestBody NewUser newUser);
}
