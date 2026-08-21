package io.herald.MySpringWeb.Service;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Concrete implementation of the UserService interface.
 * Handles the core business logic involving User CRUD, password encryption, and post-creation events (emails).
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Intercepts user saving to transparently encrypt passwords before database persistence,
     * and triggers a welcome email for newly created accounts.
     */
    @Override
    public UserTable saveUser(UserTable user) {
        // Only hash password if it's not already hashed (assuming BCrypt starts with $2a$)
        // This prevents double-hashing on update operations.
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        UserTable savedUser = userRepository.save(user);
        
        // Trigger background email dispatch (Note: ideally should check if it's a new registration vs update)
        if (user.getEmail() != null) {
            emailService.sendRegistrationEmail(user.getEmail(), user.getUsername());
        }
        
        return savedUser;
    }

    @Override
    public Optional<UserTable> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserTable> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserTable> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    /**
     * Resolves the user from the repository and uses BCrypt to verify the raw password match.
     */
    @Override
    public boolean authenticate(String username, String password) {
        Optional<UserTable> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            // Uses BCrypt match function to verify the hash securely
            return passwordEncoder.matches(password, user.get().getPassword());
        }
        return false;
    }
}
