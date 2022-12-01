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
    if(!isCredentialsValid(credentials)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    return service.connect(credentials);
  }

  @PostMapping("/users")
  ResponseEntity<User> createOneUser(@RequestBody NewUser newUser){
    if(hasNewUserNotCorrectFields(newUser)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    return service.createOneUser(newUser);
  }

  @GetMapping("/users")
  User getOneUserByEmail(@RequestParam String email){
    return service.getUserByEmail(email);
  }

  @PutMapping("/users")
  void updateUserPassword(@RequestBody Credentials credentials, @RequestHeader("Authorization") String token ){
    if(!isCredentialsValid(credentials)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    String emailFromToken = service.verifyToken(token);
    //check if the email exists
    service.getUserByEmail(credentials.getEmail());

    if(!emailFromToken.equals(credentials.getEmail())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.updateUserCredentials(credentials.getEmail(), new Credentials(credentials.getEmail(),credentials.getPassword()));
  }

  @GetMapping("/users/{id}")
  User getOneUserById(@PathVariable int id, @RequestHeader("Authorization") String token){
    service.verifyToken(token);
    return service.getUserById(id);
  }

  @PutMapping("/users/{id}")
  void updateOneUser(@PathVariable int id, @RequestBody User user, @RequestHeader("Authorization") String token){
    if(hasUserNotCorrectFields(user) || id != user.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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

  @GetMapping("/users/{id}/driver")
  Iterable<Trip> getDriverTrips(@PathVariable int id, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    service.getUserById(id);
    if(user.getId() != id) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    return service.getDriverTrips(id);
  }

  @GetMapping("/users/{id}/passenger")
  PassengerTrips getPassengerTrips(@PathVariable int id, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    service.getUserById(id);
    if(user.getId() != id) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    return service.getPassengerTrips(id);
  }

  @GetMapping("/users/{id}/notifications")
  Iterable<Notification> getNotificationsFromOneUser(@PathVariable int id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    service.getUserById(id);
    if(service.getUserByEmail(emailFromToken).getId() != id) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN);
    return service.getAllNotificationsFromUser(id);
  }

  @DeleteMapping("/users/{id}/notifications")
  void deleteAllNotificationsFromOneUser(@PathVariable int id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    service.getUserById(id);
    if(service.getUserByEmail(emailFromToken).getId() != id) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN);
    service.deleteAllNotificationsFromUser(id);
  }

  @PostMapping("/trips")
  ResponseEntity<Trip> createOneTrip(@RequestBody NewTrip newTrip, @RequestHeader("Authorization") String token){
    if(hasNewTripNotCorrectFields(newTrip)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    String emailFromToken = service.verifyToken(token);
    if(!service.getUserByEmail(emailFromToken).getId().equals(newTrip.getDriverId())) throw new ResponseStatusException(
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
    try{
      service.removeAllPassengersFromTrip(id);
    }catch(ResponseStatusException e){
      if(!e.getStatus().equals(HttpStatus.NOT_FOUND)) throw e;
    }
    service.deleteTrip(id);
  }

  @GetMapping("/trips/{id}/passengers")
  Passengers getPassengersOfATripById(@PathVariable int id){
    service.getTripById(id);
    return service.getPassengersOfATripById(id);
  }

  @PostMapping("/trips/{trip_id}/passengers/{passenger_id}")
  void addPassengerToATrip(@PathVariable("trip_id") int tripId, @PathVariable("passenger_id") int passengerId,
                           @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    service.getUserById(passengerId);
    User user = service.getUserByEmail(email);
    if(passengerId != user.getId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    Trip trip = service.getTripById(tripId);
    String notifMessage = user.getFirstname() + " " + user.getLastname() + " veut rejoindre votre voyage";
    service.createNotification(new NewNotification(trip.getDriverId(), tripId, LocalDate.now(), notifMessage));
    service.addPassengerToATrip(tripId, passengerId);
  }

  @GetMapping("/trips/{tripId}/passengers/{passengerId}")
  String getPassengerStatus(@PathVariable int tripId, @PathVariable int passengerId,
                            @RequestHeader("Authorization") String token){
    //TODO : verify if it is the good one who's connected
    service.verifyToken(token);
    service.getTripById(tripId);
    service.getUserById(passengerId);
    return service.getPassengerStatus(tripId, passengerId);
  }

  @PutMapping("/trips/{trip_id}/passengers/{user_id}")
  void updatePassengerStatus(@PathVariable("trip_id") int tripId, @PathVariable("user_id") int passengerId,
                             @RequestParam("status") String status, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    if(!status.equals("accepted") && !status.equals("refused")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    Trip trip = service.getTripById(tripId);
    if(service.getUserByEmail(email).getId() != trip.getDriverId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.updatePassengerStatus(tripId, passengerId, status);
  }

  @DeleteMapping("/trips/{trip_id}/passengers/{user_id}")
  void removePassengerFromTrip(@PathVariable("trip_id") int tripId, @PathVariable("user_id") int passengerId,
                               @RequestHeader("Authorization") String token){
    service.removePassengerFromTrip(tripId, passengerId);
  }

  private boolean isEmailValid(String email){
    return email.trim().length() != 0 && email.contains("@");
  }

  private boolean isStringEmpty(String string){
    return string.trim().length() == 0;
  }

  private boolean isCredentialsValid(Credentials insecureCredentials){
    return isEmailValid(insecureCredentials.getEmail()) && !isStringEmpty(insecureCredentials.getPassword());
  }

  private boolean isPositionValid(Position position){
    return position.getLatitude() != null && position.getLongitude() != null;
  }

  private boolean hasUserNotCorrectFields(User user){
    return !isEmailValid(user.getEmail()) ||
            isStringEmpty(user.getLastname()) ||
            isStringEmpty(user.getFirstname()) ||
            user.getId() == null;
  }

  private boolean hasNewUserNotCorrectFields(NewUser newUser){
    return !isEmailValid(newUser.getEmail()) ||
            !isCredentialsValid(newUser.getCredentials()) ||
            isStringEmpty(newUser.getLastname()) ||
            isStringEmpty(newUser.getFirstname()) ||
            isStringEmpty(newUser.getPassword());
  }

  private boolean hasNewTripNotCorrectFields(NewTrip newTrip){
    return newTrip.getDeparture() == null ||
            newTrip.getAvailableSeating() == null ||
            newTrip.getDriverId() == null ||
            !isPositionValid(newTrip.getOrigin()) ||
            !isPositionValid(newTrip.getDestination());
  }


}
