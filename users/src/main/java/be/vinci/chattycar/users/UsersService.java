package be.vinci.chattycar.users;

import be.vinci.chattycar.users.data.UsersRepository;
import be.vinci.chattycar.users.models.NewUser;
import be.vinci.chattycar.users.models.User;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
  private final UsersRepository repository;

  public UsersService(UsersRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a user
   * @param newUser User to create
   * @return user created if the user could be created, null if another user exists with this email
   */
  public User createOne(NewUser newUser) {
    if (repository.existsByEmail(newUser.getEmail())) return null;
    User user = newUser.toUser();
    repository.save(user);
    return user;
  }

  /**
   * Reads a user by email
   * @param email Email of the user
   * @return The user found, or null if the user couldn't be found
   */
  public User readOneByEmail(String email) {
    return repository.findByEmail(email);
  }

  /**
   * Reads a user by id
   * @param id Id of the user
   * @return The user found, or null if the user couldn't be found
   */
  public User readOneById(int id) {
    return repository.findById(id).orElse(null);
  }

  /**
   * Updates a user
   * @param user User to update
   * @return True if the user could be updated, false if the user couldn't be found
   */
  public boolean updateOne(User user) {
    if (!repository.existsById(user.getId())) return false;
    repository.save(user);
    return true;
  }

  /**
   * Deletes a user
   * @param id Id of the user
   * @return True if the user could be deleted, false if the user couldn't be found
   */
  public boolean deleteOneById(int id) {
    if (!repository.existsById(id)) return false;
    repository.deleteById(id);
    return true;
  }
}
