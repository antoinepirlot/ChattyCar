package be.vinci.chattycar.notifications.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Position {
  private double latitude;
  private double longitude;
}
