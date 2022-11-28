package be.vinci.chattycar.passengers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InscriptionNotFound404Exception extends ResponseStatusException {

  public InscriptionNotFound404Exception() {
    super(HttpStatus.NOT_FOUND, "No inscription could be found");
  }

}

