package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.chattycar.gateway.data.TripsProxy;
import be.vinci.chattycar.gateway.data.UsersProxy;
import be.vinci.chattycar.gateway.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class GatewayService {

  //TODO : appeler auth/verify au début de la plupart des routes

  private final AuthenticationProxy authenticationProxy;
  private final UsersProxy usersProxy;
  private final TripsProxy tripsProxy;

  public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy, TripsProxy tripsProxy) {
    this.authenticationProxy = authenticationProxy;
    this.usersProxy = usersProxy;
    this.tripsProxy = tripsProxy;
  }

  public String connect(Credentials credentials) {
    return authenticationProxy.connect(credentials);
  }

  public ResponseEntity<User> createOneUser(NewUser newUser){
    //TODO : demander au prof comment faire une transaction
    //TODO : gérer les cas en fct de la réponse
    ResponseEntity<Void> responseAuth =
            authenticationProxy.createOne(newUser.getEmail(), newUser.getInsecureCredentials());
    ResponseEntity<User> responseUser = usersProxy.createOne(newUser);
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


}
