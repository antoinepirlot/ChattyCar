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

  //TODO : appeler auth/verify au début de la plupart des routes

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
    //TODO : gérer les cas en fct de la réponse
    ResponseEntity<User> responseUser = usersProxy.createOne(newUser);
    ResponseEntity<Void> responseAuth = authenticationProxy.createOne(newUser.getEmail(), newUser.getInsecureCredentials());
    return responseUser;
  }

  public void updateUserCredentials(String email, InsecureCredentials credentials){
    authenticationProxy.updateOne(email, credentials);
  }

  public User getUserByEmail(String email){
    return usersProxy.getOneByEmail(email);
  }

  public User getUserById(int id){
    return usersProxy.getOneById(id);
  }

  public void updateUser(User user){
    usersProxy.updateOne(user.getId(), user);
  }

  public void deleteUser(int id){
    usersProxy.deleteOne(id);
  }

  public Trip createOneTrip(NewTrip newTrip){
    return tripsProxy.createOne(newTrip).getBody();
  }

  public Trip getTripById(int id){
    return tripsProxy.getOneById(id);
  }

  public void deleteTrip(int id){
    tripsProxy.deleteOne(id);
  }

  public List<Passengers> getPassengersOfATripById(int id){
    return passengersProxy.getAllPassengersFromATrip(id);
  }

  public void addPassengerToATrip(int tripId, int passengerId){
    passengersProxy.addPassengerToATrip(tripId, new Passengers(passengerId, passengerId, "pending")); //TODO : changer id ??
  }

  public Iterable<Notification> getAllNotificationsFromUser(int id){
    return notificationProxy.readFromUser(id);
  }

  public void deleteAllNotificationsFromUser(int id){
    notificationProxy.deleteFromUser(id);
  }

  public String getPassengerStatus(int tripId, int userId){
    return passengersProxy.getPassengerStatus(tripId, userId);
  }

  public void updatePassengerStatus(int tripId, int userId, String status){
    passengersProxy.updatePassengerStatus(tripId, userId, status);
  }

  public void removePassengerFromTrip(int tripId, int userId){
    passengersProxy.removePassengerFromTrip(tripId, userId);
  }




}