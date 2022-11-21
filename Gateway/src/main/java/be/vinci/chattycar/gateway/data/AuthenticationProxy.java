package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "authentication")
public interface AuthenticationProxy {
  @PostMapping("/authentication/connect")
  String connect(@RequestBody Credentials credentials);
}
