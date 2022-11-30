package be.vinci.chattycar.passengers.data;

import be.vinci.chattycar.passengers.models.Passenger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PassengersRepository extends CrudRepository<Passenger, Long> {

  boolean existsByUserIdAndTripId(Integer tripId, Integer userId);
  boolean existsByTripId(Integer tripId);
  boolean existsByUserId(Integer userId);

  Optional<Passenger> findByUserIdAndTripId(long tripId, long userId);

  @Transactional
  void deleteByTripIdAndUserId(Integer tripId, Integer userId);

  @Transactional
  void deleteByUserId(Integer userId);

  @Transactional
  void deleteByTripId(Integer tripId);

  List<Passenger> getAllPassengersByUserId(Integer userId);

  List<Passenger> getAllPassengersByTripId(Integer tripId);

}