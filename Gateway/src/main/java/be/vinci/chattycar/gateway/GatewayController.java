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
  void updateUserPassword(@RequestBody Credentials credentials, @RequestHeader("Authorization") String token ){
    String emailFromToken = service.verifyToken(token);
    //check if the email exists
    service.getUserByEmail(credentials.getEmail());

    if(!emailFromToken.equals(credentials.getEmail())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.updateUserCredentials(credentials.getEmail(), new InsecureCredentials(credentials.getEmail(),credentials.getPassword()));
  }

  @GetMapping("/users")
  User getOneUserByEmail(@RequestParam String email){
    return service.getUserByEmail(email);
  }

  @GetMapping("/users/{id}")
  User getOneUserById(@PathVariable int id, @RequestHeader("Authorization") String token){
    service.verifyToken(token);
    return service.getUserById(id);
  }

  @PutMapping("/users/{id}")
  void updateOneUser(@PathVariable int id, @RequestBody User user, @RequestHeader("Authorization") String token){
    if(id != user.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    service.getUserById(id);
    String emailFromToken = service.verifyToken(token);
    if(!emailFromToken.equals(user.getEmail())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.updateUser(user);
  }

  @DeleteMapping("/users/{id}")
  void deleteOneUser(@PathVariable int id){
    service.deleteUser(id);
  }

  //TODO : en dessous, il faut tester

  @GetMapping("/users/{id}/notifications")
  Iterable<Notification> getNotificationsFromOneUser(@PathVariable int id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    if(service.getUserByEmail(emailFromToken).getId() != id) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN);
    return service.getAllNotificationsFromUser(id);
  }

  @DeleteMapping("/users/{id}/notifications")
  void deleteAllNotificationsFromOneUser(@PathVariable int id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    if(service.getUserByEmail(emailFromToken).getId() != id) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN);
    service.deleteAllNotificationsFromUser(id);
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

  @PostMapping("/trips/{tripId}/passengers/{passengerId}")
  void addPassengerToATrip(@PathVariable int tripId, @PathVariable int passengerId){
    service.addPassengerToATrip(tripId, passengerId);
  }

  @GetMapping("/trips/{tripId}/passengers/{passengerId}")
  String getPassengerStatus(@PathVariable int tripId, @PathVariable int passengerId){
    return service.getPassengerStatus(tripId, passengerId);
  }

  @PutMapping("/trips/{trip_id}/passengers/{user_id}")
  void updatePassengerStatus(@PathVariable("trip_id") int tripId, @PathVariable("user_id") int passengerId,  @RequestParam("status") String status){
    service.updatePassengerStatus(tripId, passengerId, status);
  }

  @DeleteMapping("/trips/{trip_id}/passengers/{user_id}")
  void removePassengerFromTrip(@PathVariable("trip_id") int tripId, @PathVariable("user_id") int passengerId){
    service.removePassengerFromTrip(tripId, passengerId);
  }


}
