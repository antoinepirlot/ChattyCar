package be.vinci.chattycar.gateway;

import be.vinci.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.chattycar.gateway.models.Credentials;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

  private final AuthenticationProxy authenticationProxy;

  public GatewayService(AuthenticationProxy authenticationProxy) {
    this.authenticationProxy = authenticationProxy;
  }

  public String connect(Credentials credentials) {
    return authenticationProxy.connect(credentials);
  }
}
