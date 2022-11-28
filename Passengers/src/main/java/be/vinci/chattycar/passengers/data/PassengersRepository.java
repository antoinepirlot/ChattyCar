package be.vinci.chattycar.passengers.data;

import be.vinci.chattycar.passengers.models.Passenger;
import be.vinci.chattycar.passengers.models.PassengerTrips;
import be.vinci.chattycar.passengers.models.Passengers;
import be.vinci.chattycar.passengers.models.Trip;
import javax.persistence.criteria.CriteriaBuilder.In;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PassengersRepository extends CrudRepository<Passenger, Long> {

  boolean existsByIds(Integer tripId, Integer userId);
  boolean existsById(Integer tripId);

  boolean tripExists(Integer tripId);
  boolean userExists(Integer userId);
  Optional<Passenger> findByIds(long tripId, long userId);

  Trip getTrip(Integer tripId);

  @Transactional
  void deleteByIds(Integer tripId, Integer userId);

  @Transactional
  void deleteByUserId(Integer userId);

  @Transactional
  void deleteByTripId(Integer tripId);

  PassengerTrips findAllTripsByUserId(Integer userId);

  Passengers findAllPassengersByTripId(Integer tripId);

}