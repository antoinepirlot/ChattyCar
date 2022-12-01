package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.NewTrip;
import be.vinci.chattycar.gateway.models.PassengerTrips;
import be.vinci.chattycar.gateway.models.Trip;
import be.vinci.chattycar.gateway.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Repository
@FeignClient(name = "trips")
public interface TripsProxy {

    @PostMapping("/trips")
    ResponseEntity<Trip> createOne(@RequestBody NewTrip newTrip);

    @GetMapping("/trips")
    Iterable<Trip> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) Double originLat,
            @RequestParam(required = false) Double originLon,
            @RequestParam(required = false) Double destinationLat,
            @RequestParam(required = false) Double destinationLon
    );

    @GetMapping("/trips/{id}")
    Trip getOneById(@PathVariable int id);

    @DeleteMapping("/trips/{id}")
    void deleteOne(@PathVariable int id);

    @GetMapping("/trips/{id}/driver")
    Iterable<Trip> getDriverTrips(@PathVariable int id);

    @GetMapping("/trips/{id}/passenger")
    PassengerTrips getPassengerTrips(@PathVariable int id);

}
