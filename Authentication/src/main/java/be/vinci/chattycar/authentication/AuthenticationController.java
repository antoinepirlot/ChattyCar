package be.vinci.chattycar.authentication;

import be.vinci.chattycar.authentication.models.InsecureCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {
  private final AuthenticationService service;

  public AuthenticationController(AuthenticationService service) {
    this.service = service;
  }

  @PostMapping({"/authentication/connect"})
  public String connect(@RequestBody InsecureCredentials credentials) {
    String token = this.service.connect(credentials);
    if (token == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } else {
      return token;
    }
  }

  @PostMapping({"/authentication/verify"})
  public String verify(@RequestBody String token) {
    String email = this.service.verify(token);
    if (email == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } else {
      return email;
    }
  }

  @PostMapping({"/authentication/{email}"})
  public ResponseEntity<Void> createOne(@PathVariable String email, @RequestBody InsecureCredentials credentials) {
    if (credentials.getEmail() != null && credentials.getPassword() != null && credentials.getEmail().equals(email)) {
      boolean created = this.service.createOne(credentials);
      if (!created) {
        throw new ResponseStatusException(HttpStatus.CONFLICT);
      } else {
        return new ResponseEntity(HttpStatus.CREATED);
      }
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping({"/authentication/{email}"})
  public void updateOne(@PathVariable String email, @RequestBody InsecureCredentials credentials) {
    if (credentials.getEmail() != null && credentials.getPassword() != null && credentials.getEmail().equals(email)) {
      boolean found = this.service.updateOne(credentials);
      if (!found) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping({"/authentication/{email}"})
  public void deleteCredentials(@PathVariable String email) {
    boolean found = this.service.deleteOne(email);
    if (!found) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
}

