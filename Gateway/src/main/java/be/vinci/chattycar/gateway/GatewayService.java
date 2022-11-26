package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.chattycar.gateway.data.UsersProxy;
import be.vinci.chattycar.gateway.models.Credentials;
import be.vinci.chattycar.gateway.models.InsecureCredentials;
import be.vinci.chattycar.gateway.models.NewUser;
import be.vinci.chattycar.gateway.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

  private final AuthenticationProxy authenticationProxy;
  private final UsersProxy usersProxy;

  public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy) {
    this.authenticationProxy = authenticationProxy;
    this.usersProxy = usersProxy;
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


}
