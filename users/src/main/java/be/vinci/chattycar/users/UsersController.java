package be.vinci.chattycar.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createOne(@RequestBody NewUser newUser) {
        if (newUser.getEmail() == null || newUser.getEmail().equals("") ||
            newUser.getFirstname() == null || newUser.getFirstname().equals("") ||
            newUser.getLastname() == null || newUser.getLastname().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User userCreated = service.createOne(newUser);
        if (userCreated==null) throw new ResponseStatusException(HttpStatus.CONFLICT);
        return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public User readOne(@RequestParam String email) {
        User user = service.readOne(email);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return user;
    }

}
