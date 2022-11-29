package be.vinci.chattycar.passengers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TripNotFound404Exception extends ResponseStatusException {

  public TripNotFound404Exception() {
    super(HttpStatus.NOT_FOUND, "No trip could be found");
  }

}

