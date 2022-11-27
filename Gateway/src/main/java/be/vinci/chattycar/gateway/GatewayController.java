package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
  User getOneUserByEmail(@RequestParam String email){
    return service.getUserByEmail(email);
  }

  @GetMapping("/users/{id}")
  User getOneUserById(@PathVariable int id){
    return service.getUserById(id);
  }

  @PutMapping("/users/{id}")
  void updateOneUser(@PathVariable int id, @RequestBody User user){
    if(id != user.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    service.updateUser(user);
  }

  @DeleteMapping("/users/{id}")
  void deleteOneUser(@PathVariable int id){
    service.deleteUser(id);
  }

  @PostMapping("/trips")
  Trip createOneTrip(@RequestBody NewTrip newTrip){
    return service.createOneTrip(newTrip);
  }

  @GetMapping("/trips/{id}")
  Trip getOneTripById(@PathVariable int id){
    return service.getTripById(id);
  }

  @DeleteMapping("/trips/{id}")
  void deleteOneTrip(@PathVariable int id){
    service.deleteTrip(id);
  }

  @GetMapping("/trips/{id}/passengers")
  List<Passengers> getPassengersOfATripById(@PathVariable int id){
    return service.getPassengersOfATripById(id);
  }

  @PostMapping("/trips/{tripId}/passengers/{passenger_id}")
  void addPassengerToATrip(@PathVariable int tripId, @PathVariable int passengerId){
    service.addPassengerToATrip(tripId, passengerId);
  }



}
