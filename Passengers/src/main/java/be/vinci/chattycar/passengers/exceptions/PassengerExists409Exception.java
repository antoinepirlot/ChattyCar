package be.vinci.chattycar.passengers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PassengerExists409Exception extends ResponseStatusException {
  public PassengerExists409Exception() {
    super(HttpStatus.CONFLICT, "This user is already a passenger for this trip");
  }
}


