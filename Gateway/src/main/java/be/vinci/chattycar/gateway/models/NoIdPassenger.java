package be.vinci.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoIdPassenger {
  private Integer tripId;
  private Integer userId;
  private String status;
}

