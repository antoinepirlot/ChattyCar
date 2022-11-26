package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.NewTrip;
import be.vinci.chattycar.gateway.models.Trip;
import be.vinci.chattycar.gateway.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "trips")
public interface TripsProxy {

    @PostMapping("/trips")
    ResponseEntity<Trip> createOne(@RequestBody NewTrip newTrip);

    @GetMapping("/trips/{id}")
    Trip getOneById(@PathVariable int id);

    @DeleteMapping("/trips/{id}")
    void deleteOne(@PathVariable int id);
}
