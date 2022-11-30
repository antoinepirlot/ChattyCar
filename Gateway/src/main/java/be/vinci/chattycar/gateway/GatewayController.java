package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
  void deleteOneUser(@PathVariable int id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    //check if the email exists
    User userFromDB = service.getUserByEmail(emailFromToken);

    if(userFromDB.getId() != id) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    //TODO : supprimer tous les trips, etc.

    service.deleteUser(id);
  }

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
  Trip createOneTrip(@RequestBody NewTrip newTrip, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    if(service.getUserByEmail(emailFromToken).getId() != newTrip.getDriverId()) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN);
    return service.createOneTrip(newTrip);
  }

  @GetMapping("/trips/{id}")
  Trip getOneTripById(@PathVariable int id){
    return service.getTripById(id);
  }

  @DeleteMapping("/trips/{id}")
  void deleteOneTrip(@PathVariable int id, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    Trip trip = service.getTripById(id);
    if(trip.getDriverId() != user.getId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.deleteTrip(id);
  }

  @GetMapping("/trips/{id}/passengers")
  Passengers getPassengersOfATripById(@PathVariable int id){
    service.getTripById(id);
    return service.getPassengersOfATripById(id);
  }

  @PostMapping("/trips/{trip_id}/passengers/{passenger_id}")
  NoIdPassenger addPassengerToATrip(@PathVariable("trip_id") int tripId, @PathVariable("passenger_id") int passengerId,
                           @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    System.out.println(12);
    service.getUserById(passengerId);
    System.out.println(12);
    User user = service.getUserByEmail(email);
    System.out.println(12);
    if(passengerId != user.getId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    System.out.println(12);
    Trip trip = service.getTripById(tripId);
    System.out.println(12);
    String notifMessage = user.getFirstname() + " " + user.getLastname() + " veut rejoindre votre voyage";
    service.createNotification(new NewNotification(trip.getDriverId(), tripId, LocalDate.now(), notifMessage));
    System.out.println(12);
    return service.addPassengerToATrip(tripId, passengerId);
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
