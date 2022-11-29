package be.vinci.chattycar.passengers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSeatLeft400Exception extends ResponseStatusException {
  public NoSeatLeft400Exception() {
    super(HttpStatus.BAD_REQUEST, "There is no seat left on this trip");
  }
}


