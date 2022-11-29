package be.vinci.chattycar.passengers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InscriptionNotValidException extends ResponseStatusException {

  public InscriptionNotValidException() {
    super(HttpStatus.BAD_REQUEST, "User is not a passenger, or is not in pending status, or status not in accepted value");
  }

}

