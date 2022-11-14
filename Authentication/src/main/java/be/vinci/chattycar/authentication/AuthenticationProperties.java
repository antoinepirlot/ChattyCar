package be.vinci.chattycar.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "be.vinci.chattycar.authentication")
@Getter
@Setter
public class AuthenticationProperties {

    private String secret;

}
