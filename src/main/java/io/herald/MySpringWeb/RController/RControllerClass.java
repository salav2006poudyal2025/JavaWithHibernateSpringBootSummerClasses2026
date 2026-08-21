package io.herald.MySpringWeb.RController;

import io.herald.MySpringWeb.Exception.UserNotFoundException;
import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RControllerClass {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserTable>> getAllUsers() {
        List<UserTable> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserTable> getUserById(@PathVariable int id) {
        Optional<UserTable> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<String> saveUser(@RequestBody UserTable user) {
        userService.saveUser(user);
        return new ResponseEntity<>("Saved Successfully", HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserTable> updateUser(@PathVariable int id, @RequestBody UserTable userDetails) {
        Optional<UserTable> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            UserTable user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(userDetails.getPassword());
            }
            UserTable updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        Optional<UserTable> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }
}
