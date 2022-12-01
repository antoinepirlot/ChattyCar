package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.chattycar.gateway.data.NotificationProxy;
import be.vinci.chattycar.gateway.data.PassengersProxy;
import be.vinci.chattycar.gateway.data.TripsProxy;
import be.vinci.chattycar.gateway.data.UsersProxy;
import be.vinci.chattycar.gateway.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
   * @param tripId Id of the trip
   * @param passengerId Id of the passenger
   */
  public void addPassengerToATrip(int tripId, int passengerId){
    passengersProxy.addPassengerToATrip(tripId, passengerId).getBody();
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

  public void updatePassengerStatus(int tripId, int userId, String status){
    passengersProxy.updatePassengerStatus(tripId, userId, status);
  }

  /**
   * Deletes a passenger from a trip
   * @param tripId Id of the trip
   * @param passengerId Id of the passenger
   */
  public void removePassengerFromTrip(int tripId, int passengerId){
    passengersProxy.removePassengerFromTrip(tripId, passengerId);
  }

  /**
   * Deletes all passengers from a trip
   * @param tripId Id of the trip
   */
  public void removeAllPassengersFromTrip(int tripId){
    passengersProxy.removeAllPassengersFromTrip(tripId);
  }



  public Notification createNotification(NewNotification newNotification){
    return notificationProxy.createOne(newNotification).getBody();
  }

  public Iterable<Trip> getDriverTrips(int id){
    return tripsProxy.getDriverTrips(id);
  }

  public PassengerTrips getPassengerTrips(int id){
    return tripsProxy.getPassengerTrips(id);
  }



}
