package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.Passengers;
import be.vinci.chattycar.gateway.models.Trip;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@FeignClient(name = "passengers")
public interface PassengersProxy {

    @GetMapping("/passengers/{tripId}")
    List<Passengers> getAllPassengersFromATrip(@PathVariable int tripId);

    @PostMapping("/passengers/{tripId}")
    void addPassengerToATrip(@PathVariable int tripId, @RequestBody Passengers passenger);

    @PutMapping("/passengers/{id}")
    void updatePassenger(@PathVariable int id, @RequestBody Passengers passenger);
}
