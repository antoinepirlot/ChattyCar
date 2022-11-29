package be.vinci.chattycar.passengers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PassengerExists400Exception extends ResponseStatusException {
  public PassengerExists400Exception() {
    super(HttpStatus.BAD_REQUEST, "This user is already a passenger for this trip");
  }
}


