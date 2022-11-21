package be.vinci.chattycar.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
    prefix = "be.vinci.chattycar.authentication"
)
public class AuthenticationProperties {
  private String secret;

  public AuthenticationProperties() {
  }

  public String getSecret() {
    return this.secret;
  }

  public void setSecret(final String secret) {
    this.secret = secret;
  }
}
