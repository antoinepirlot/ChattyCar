package be.vinci.chattycar.passengers.data;

import be.vinci.chattycar.passengers.models.Passenger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PassengersRepository extends CrudRepository<Passenger, Integer> {

  boolean existsByUserIdAndTripId(Integer tripId, Integer userId);
  boolean existsByTripId(Integer tripId);
  boolean existsByUserId(Integer userId);

  Optional<Passenger> findByUserIdAndTripId(Integer tripId, Integer userId);


  void deleteByUserIdAndTripId(Integer tripId, Integer userId);

  void deleteByUserId(Integer userId);

  void deleteByTripId(Integer tripId);

  List<Passenger> getAllPassengersByUserId(Integer userId);

  List<Passenger> getAllPassengersByTripId(Integer tripId);

}