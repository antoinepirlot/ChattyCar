package be.vinci.chattycar.users;

public class UsersService {
  private final UsersRepository repository;

  public UsersService(UsersRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a user
   * @param user User to create
   * @return true if the user could be created, false if another user exists with this pseudo
   */
  public boolean createOne(NewUser user) {
    if (repository.existsByEmail(user.getEmail())) return false;
    repository.save(user.toUser());
    return true;
  }
}
