package be.vinci.chattycar.gateway.data;

import be.vinci.chattycar.gateway.models.NewTrip;
import be.vinci.chattycar.gateway.models.Trip;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "trips")
public interface TripsProxy {

    @PostMapping("/trips")
    ResponseEntity<Trip> createOne(@RequestBody NewTrip newTrip);
}
