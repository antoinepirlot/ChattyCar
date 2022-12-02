package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.NoIdPassenger;
import be.vinci.chattycar.gateway.models.Passengers;
import be.vinci.chattycar.gateway.models.Trip;
import javax.ws.rs.QueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@FeignClient(name = "passengers")
public interface PassengersProxy {

    @GetMapping("/passengers/{trip_id}")
    Passengers getAllPassengersFromATrip(@PathVariable("trip_id") Integer tripId);

    @PostMapping("/passengers/{trip_id}/user/{user_id}")
    ResponseEntity<NoIdPassenger> addPassengerToATrip(@PathVariable("trip_id") Integer tripId, @PathVariable("user_id") Integer userId );

    @PutMapping("/passengers/{id}")
    void updatePassenger(@PathVariable int id, @RequestBody Passengers passenger);

    @GetMapping("/passengers/{trip_id}/user/{user_id}")
    String getPassengerStatus(@PathVariable("trip_id") int tripId, @PathVariable("user_id") int userId);

    @PutMapping("/passengers/{trip_id}/user/{user_id}")
    ResponseEntity<Void> updatePassengerStatus(@PathVariable("trip_id") Integer tripId,
        @PathVariable("user_id") Integer userId, @RequestParam("status") String status);

    @DeleteMapping("/passengers/{trip_id}/user/{user_id}")
    void removePassengerFromTrip(@PathVariable("trip_id") int tripId, @PathVariable("user_id") int userId);

    @DeleteMapping("/passengers/{trip_id}")
    ResponseEntity<Void> removeAllPassengersFromTrip(@PathVariable("trip_id") int tripId);
}
