package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.Passengers;
import be.vinci.chattycar.gateway.models.Trip;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PassengersProxy {

    @GetMapping("/passengers/{tripId}")
    List<Passengers> getAllPassengersFromATrip(@PathVariable int id);

    @PostMapping("/passengers/{tripId}")
    void addPassengerToATrip(@PathVariable int id, @RequestBody Passengers passenger);

    @PutMapping("/passengers/{id}")
    void updatePassenger(@PathVariable int id, @RequestBody Passengers passenger);
}
