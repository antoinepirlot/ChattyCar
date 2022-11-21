package be.vinci.chattycar.users;

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
   * @return used created if the user could be created, null if another user exists with this pseudo
   */
  public User createOne(NewUser newUser) {
    if (repository.existsByEmail(newUser.getEmail())) return null;
    User user = newUser.toUser();
    repository.save(user);
    return user;
  }
}
