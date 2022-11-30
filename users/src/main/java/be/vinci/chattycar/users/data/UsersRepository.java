package be.vinci.chattycar.users.data;

import be.vinci.chattycar.users.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {

  boolean existsByEmail(String email);
  User findByEmail(String email);
}
