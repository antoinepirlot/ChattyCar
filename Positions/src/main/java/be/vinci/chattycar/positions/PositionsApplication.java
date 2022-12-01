package be.vinci.chattycar.positions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PositionsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PositionsApplication.class, args);
  }

}
