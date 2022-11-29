package be.vinci.chattycar.users;

import be.vinci.chattycar.users.models.NewUser;
import be.vinci.chattycar.users.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public User readOneByEmail(@RequestParam String email) {
        User user = service.readOneByEmail(email);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return user;
    }

    @GetMapping("/users/{id}")
    public User readOneById(@PathVariable int id) {
        User user = service.readOneById(id);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return user;
    }

    @PutMapping("/users/{id}")
    public void updateOne(@PathVariable int id, @RequestBody User user) {
        if (user.getId() <1 || user.getId() !=id || user.getEmail().equals("") ||
            user.getFirstname() == null || user.getFirstname().equals("") ||
            user.getLastname() == null || user.getLastname().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        boolean userFound = service.updateOne(user);
        if (!userFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users/{id}")
    public void deleteOne(@PathVariable int id) {
        boolean userFound = service.deleteOneById(id);
        if (!userFound) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
