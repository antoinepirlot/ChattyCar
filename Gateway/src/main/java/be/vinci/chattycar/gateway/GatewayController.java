package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.models.Credentials;
import be.vinci.chattycar.gateway.models.InsecureCredentials;
import be.vinci.chattycar.gateway.models.NewUser;
import be.vinci.chattycar.gateway.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = { "http://localhost:80" })
@RestController
public class GatewayController {
  private final GatewayService service;
  public GatewayController(GatewayService service) {
    this.service = service;
  }

  @PostMapping("/auth")
  String connect(@RequestBody Credentials credentials) {
    return service.connect(credentials);
  }

  @PostMapping("/users")
  ResponseEntity<User> createOneUser(@RequestBody NewUser newUser){
    return service.createOneUser(newUser);
  }

  @PutMapping("/users")
  void updateUserPassword(@RequestBody Credentials credentials){
    InsecureCredentials insecureCredentials = new InsecureCredentials();
    insecureCredentials.setEmail(credentials.getEmail());
    insecureCredentials.setPassword(credentials.getPassword());
    service.updateUserCredentials(credentials.getEmail(), insecureCredentials);
  }

  @GetMapping("/users")
  User getOneByEmail(@RequestParam String email){
    return service.getUserByEmail(email);
  }

  @GetMapping("/users")
  User getOneById(@RequestParam int id){
    return service.getUserById(id);
  }

  @PutMapping("/users/{id}")
  void updateOne(@PathVariable int id, @RequestBody User user){
    if(id != user.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    service.updateUser(user);
  }

  @DeleteMapping("/users/{id}")
  void deleteOne(@PathVariable int id){
    service.deleteUser(id);
  }

}
