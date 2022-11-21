package be.vinci.chattycar.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthenticationApplication {
  public AuthenticationApplication() {
  }

  public static void main(String[] args) {
    SpringApplication.run(AuthenticationApplication.class, args);
  }
}
