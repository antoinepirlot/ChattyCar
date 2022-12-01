package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.chattycar.gateway.data.NotificationProxy;
import be.vinci.chattycar.gateway.data.PassengersProxy;
import be.vinci.chattycar.gateway.data.TripsProxy;
import be.vinci.chattycar.gateway.data.UsersProxy;
import be.vinci.chattycar.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class GatewayService {

  private final AuthenticationProxy authenticationProxy;
  private final UsersProxy usersProxy;
  private final TripsProxy tripsProxy;
  private final PassengersProxy passengersProxy;
  private final NotificationProxy notificationProxy;

  public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy, TripsProxy tripsProxy, PassengersProxy passengersProxy,
      NotificationProxy notificationProxy) {
    this.authenticationProxy = authenticationProxy;
    this.usersProxy = usersProxy;
    this.tripsProxy = tripsProxy;
    this.passengersProxy = passengersProxy;
    this.notificationProxy = notificationProxy;
  }

  public String connect(Credentials credentials) {
    return authenticationProxy.connect(credentials);
  }

  public String verifyToken(String token){return authenticationProxy.verify(token);}

  public ResponseEntity<User> createOneUser(NewUser newUser){
    ResponseEntity<User> responseUser = usersProxy.createOne(newUser);
    authenticationProxy.createOne(newUser.getEmail(), newUser.getCredentials());
    return responseUser;
  }

  public void updateUserCredentials(String email, Credentials credentials){
    authenticationProxy.updateOne(email, credentials);
  }

  /**
   * Reads a user by email
   * @param email Email of the user
   * @return The user found
   */
  public User getUserByEmail(String email){
    return usersProxy.getOneByEmail(email);
  }

  /**
   * Reads a user by id
   * @param id Id of the user
   * @return The user found
   */
  public User getUserById(int id){
    return usersProxy.getOneById(id);
  }

  /**
   * Updates a user
   * @param user User to update
   */
  public void updateUser(User user){
    usersProxy.updateOne(user.getId(), user);
  }

  /**
   * Deletes a user
   * @param id Id of the user
   */
  public void deleteUser(int id){
    // delete all notifications
    try {
      notificationProxy.deleteFromUser(id);
    }catch(ResponseStatusException e){
      if(!e.getStatus().equals(HttpStatus.NOT_FOUND)) throw e;
    }
    try {
      // delete all his trips
      Iterable<Trip> trips = tripsProxy.getDriverTrips(id);
      for (Trip trip : trips) {
        try {
          passengersProxy.removeAllPassengersFromTrip(trip.getId());
        } catch (ResponseStatusException e) {
          if (!e.getStatus().equals(HttpStatus.NOT_FOUND)) throw e;
        }
        tripsProxy.deleteOne(trip.getId());
      }
    }catch(ResponseStatusException e){
      if(!e.getStatus().equals(HttpStatus.NOT_FOUND)) throw e;
    }
    // delete credentials and user
    String email = usersProxy.getOneById(id).getEmail();
    authenticationProxy.deleteCredentials(email);
    usersProxy.deleteOne(id);
  }

  /**
   * Creates a trip
   * @param newTrip Trip to create
   * @return trip created
   */
  public ResponseEntity<Trip> createOneTrip(NewTrip newTrip){
    return tripsProxy.createOne(newTrip);
  }

  /**
   * Reads a trip by id
   * @param id Id of the trip
   * @return The trip found
   */
  public Trip getTripById(int id){
    return tripsProxy.getOneById(id);
  }

  /**
   * Deletes a trip
   * @param id Id of the trip
   */
  public void deleteTrip(int id){
    try{
      passengersProxy.removeAllPassengersFromTrip(id);
    }catch(ResponseStatusException e){
      if(!e.getStatus().equals(HttpStatus.NOT_FOUND)) throw e;
    }
    tripsProxy.deleteOne(id);
  }

  /**
   * Reads all passengers from a trip
   * @param id Id of the trip
   * @return 3 lists of passengers from this trip, one for each status
   */
  public Passengers getPassengersOfATripById(int id){
    return passengersProxy.getAllPassengersFromATrip(id);
  }

  /**
   * Adds a passenger to a trip
   * @param trip the trip
   * @param passengerId Id of the passenger
   */
  public void addPassengerToATrip(Trip trip, int passengerId){
    User user = usersProxy.getOneById(passengerId);
    String notifMessage = user.getFirstname() + " " + user.getLastname() + " veut rejoindre votre voyage";
    passengersProxy.addPassengerToATrip(trip.getId(), passengerId).getBody();
    notificationProxy.createOne(new NewNotification(trip.getDriverId(), trip.getId(), LocalDate.now(), notifMessage));
  }

  /**
   * Reads all notifications from a user
   * @param id Id of the user
   * @return The list of notifications from this user
   */
  public Iterable<Notification> getAllNotificationsFromUser(int id){
    return notificationProxy.readFromUser(id);
  }

  /**
   * Deletes all notifications from a user
   * @param id Id of the user
   */
  public void deleteAllNotificationsFromUser(int id){
    notificationProxy.deleteFromUser(id);
  }

  public String getPassengerStatus(int tripId, int userId){
    return passengersProxy.getPassengerStatus(tripId, userId);
  }

  public void updatePassengerStatus(int tripId, int passengerId, String status){
    passengersProxy.updatePassengerStatus(tripId, passengerId, status);
    String messageNotif;
    if(status.equals("accepted")){
      messageNotif = "Votre demande de voyage a été acceptée";
    }
    else {
      messageNotif = "Votre demande de voyage a été refusée";
    }
    notificationProxy.createOne(new NewNotification(passengerId, tripId, LocalDate.now(), messageNotif));
  }

  /**
   * Deletes a passenger from a trip
   * @param tripId Id of the trip
   * @param passengerId Id of the passenger
   */
  public void removePassengerFromTrip(int tripId, int passengerId){
    passengersProxy.removePassengerFromTrip(tripId, passengerId);
  }

  public Iterable<Trip> getDriverTrips(int id){
    return tripsProxy.getDriverTrips(id);
  }

  public PassengerTrips getPassengerTrips(int id){
    return tripsProxy.getPassengerTrips(id);
  }

  public Iterable<Trip> getAllTripsWithSearchQuery(LocalDate departureDate, Double originLat, Double originLon, Double destinationLat, Double destinationLon){
    return tripsProxy.getAll(departureDate, originLat, originLon, destinationLat, destinationLon);
  }



}
