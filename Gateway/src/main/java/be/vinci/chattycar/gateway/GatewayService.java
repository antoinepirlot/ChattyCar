package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.chattycar.gateway.models.Credentials;
import be.vinci.chattycar.gateway.models.InsecureCredentials;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class GatewayService {

  private final AuthenticationProxy authenticationProxy;

  public GatewayService(AuthenticationProxy authenticationProxy) {
    this.authenticationProxy = authenticationProxy;
  }

  public String connect(Credentials credentials) {
    return authenticationProxy.connect(credentials);
  }

  public ResponseEntity<Void> createOneUser(String email, InsecureCredentials credentials){
    return authenticationProxy.createOne(email, credentials);
  }

  public void updateUserCredentials(String email, InsecureCredentials credentials){
    authenticationProxy.updateOne(email, credentials);
  }


}
