package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.models.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

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
  User getOneUserById(@PathVariable Integer id, @RequestHeader("Authorization") String token){
    service.verifyToken(token);
    return service.getUserById(id);
  }

  @PutMapping("/users/{id}")
  void updateOneUser(@PathVariable Integer id, @RequestBody User user, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    if(hasUserNotCorrectFields(user) || !user.getId().equals(id)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    service.getUserById(id);
    if(!emailFromToken.equals(user.getEmail())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.updateUser(user);
  }

  @DeleteMapping("/users/{id}")
  void deleteOneUser(@PathVariable Integer id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    //check if the email exists
    User userFromDB = service.getUserByEmail(emailFromToken);
    if(!userFromDB.getId().equals(id)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.deleteUser(id);
  }

  @GetMapping("/users/{id}/driver")
  Iterable<Trip> getDriverTrips(@PathVariable Integer id, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    service.getUserById(id);
    if(!user.getId().equals(id)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    return service.getDriverTrips(id);
  }

  @GetMapping("/users/{id}/passenger")
  PassengerTrips getPassengerTrips(@PathVariable Integer id, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    service.getUserById(id);
    if(!user.getId().equals(id)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    return service.getPassengerTrips(id);
  }

  @GetMapping("/users/{id}/notifications")
  Iterable<Notification> getNotificationsFromOneUser(@PathVariable Integer id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    service.getUserById(id);
    if(!service.getUserByEmail(emailFromToken).getId().equals(id)) throw new ResponseStatusException(
        HttpStatus.FORBIDDEN);
    return service.getAllNotificationsFromUser(id);
  }

  @DeleteMapping("/users/{id}/notifications")
  void deleteAllNotificationsFromOneUser(@PathVariable Integer id, @RequestHeader("Authorization") String token){
    String emailFromToken = service.verifyToken(token);
    service.getUserById(id);
    if(!service.getUserByEmail(emailFromToken).getId().equals(id)) throw new ResponseStatusException(
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

  @GetMapping("/trips")
  Iterable<Trip> getAllTripsWithSearchQuery(
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
          @RequestParam(required = false) Double originLat,
          @RequestParam(required = false) Double originLon,
          @RequestParam(required = false) Double destinationLat,
          @RequestParam(required = false) Double destinationLon){
    if((originLat == null && originLon != null ) ||
       (originLat != null && originLon == null) ||
       (destinationLat == null && destinationLon != null ) ||
       (destinationLat != null && destinationLon == null)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    return service.getAllTripsWithSearchQuery(departureDate, originLat, originLon, destinationLat, destinationLon);
  }

  @GetMapping("/trips/{id}")
  Trip getOneTripById(@PathVariable Integer id){
    return service.getTripById(id);
  }

  @DeleteMapping("/trips/{id}")
  void deleteOneTrip(@PathVariable Integer id, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    Trip trip = service.getTripById(id);
    if(!trip.getDriverId().equals(user.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.deleteTrip(id);
  }

  @GetMapping("/trips/{id}/passengers")
  Passengers getPassengersOfATripById(@PathVariable Integer id, @RequestHeader("Authorization") String token ){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    Trip trip = service.getTripById(id);
    if(!user.getId().equals(trip.getDriverId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    return service.getPassengersOfATripById(id);
  }

  @PostMapping("/trips/{trip_id}/passengers/{passenger_id}")
  void addPassengerToATrip(@PathVariable("trip_id") Integer tripId, @PathVariable("passenger_id") Integer passengerId,
                           @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    service.getUserById(passengerId);
    User user = service.getUserByEmail(email);
    if(!passengerId.equals(user.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    Trip trip = service.getTripById(tripId);
    service.addPassengerToATrip(trip, passengerId);

  }

  @GetMapping("/trips/{tripId}/passengers/{passengerId}")
  String getPassengerStatus(@PathVariable Integer tripId, @PathVariable Integer passengerId,
                            @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    Trip trip = service.getTripById(tripId);
    service.getUserById(passengerId);
    if(!user.getId().equals(passengerId) && !trip.getDriverId().equals(passengerId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    return service.getPassengerStatus(tripId, passengerId);
  }

  @PutMapping("/trips/{trip_id}/passengers/{user_id}")
  void updatePassengerStatus(@PathVariable("trip_id") Integer tripId, @PathVariable("user_id") Integer passengerId,
                             @RequestParam("status") String status, @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    if(!status.equals("accepted") && !status.equals("refused")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    Trip trip = service.getTripById(tripId);
    if(!service.getUserByEmail(email).getId().equals(trip.getDriverId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.updatePassengerStatus(tripId, passengerId, status);
  }

  @DeleteMapping("/trips/{trip_id}/passengers/{user_id}")
  void removePassengerFromTrip(@PathVariable("trip_id") Integer tripId, @PathVariable("user_id") Integer passengerId,
                               @RequestHeader("Authorization") String token){
    String email = service.verifyToken(token);
    User user = service.getUserByEmail(email);
    Trip trip = service.getTripById(tripId);
    if(!user.getId().equals(passengerId) && !user.getId().equals(trip.getDriverId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    service.removePassengerFromTrip(tripId, passengerId);
  }

  /**
   * Verify if an email is valid or not
   * @param email the email to analyze
   * @return true if email is valid false otherwise
   */
  private boolean isEmailValid(String email){
    return email.trim().length() != 0 && email.contains("@");
  }

  /**
   * Verify if a string is empty or not
   * @param string the string to analyze
   * @return true if string is empty false otherwise
   */
  private boolean isStringEmpty(String string){
    return string.trim().length() == 0;
  }

  /**
   * Verify if credentials is valid or not
   * @param credentials the credentials to analyze
   * @return true if credentials is valid false otherwise
   */
  private boolean isCredentialsValid(Credentials credentials){
    return isEmailValid(credentials.getEmail()) && !isStringEmpty(credentials.getPassword());
  }

  /**
   * Verify if position is valid or not
   * @param position the position to analyze
   * @return true if position is valid false otherwise
   */
  private boolean isPositionValid(Position position){
    return position.getLatitude() != null && position.getLongitude() != null;
  }

  /**
   * Verify if the user has at least one not correct field
   * @param user the user to analyze
   * @return true if user has at least one not correct field false otherwise
   */
  private boolean hasUserNotCorrectFields(User user){
    return !isEmailValid(user.getEmail()) ||
            isStringEmpty(user.getLastname()) ||
            isStringEmpty(user.getFirstname()) ||
            user.getId() == null;
  }

  /**
   * Verify if the new user has at least one not correct field
   * @param newUser the user to analyze
   * @return true if new user has at least one not correct field false otherwise
   */
  private boolean hasNewUserNotCorrectFields(NewUser newUser){
    return !isEmailValid(newUser.getEmail()) ||
            !isCredentialsValid(newUser.getCredentials()) ||
            isStringEmpty(newUser.getLastname()) ||
            isStringEmpty(newUser.getFirstname()) ||
            isStringEmpty(newUser.getPassword());
  }

  /**
   * Verify if the new trip has at least one not correct field
   * @param newTrip the user to analyze
   * @return true if new trip has at least one not correct field false otherwise
   */
  private boolean hasNewTripNotCorrectFields(NewTrip newTrip){
    return newTrip.getDeparture() == null ||
            newTrip.getAvailableSeating() == null ||
            newTrip.getDriverId() == null ||
            !isPositionValid(newTrip.getOrigin()) ||
            !isPositionValid(newTrip.getDestination());
  }
}
