package be.vinci.chattycar.passengers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFound404Exception extends ResponseStatusException {

  public UserNotFound404Exception() {
    super(HttpStatus.NOT_FOUND, "No user could be found");
  }

}

