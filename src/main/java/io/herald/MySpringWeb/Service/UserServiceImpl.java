package io.herald.MySpringWeb.Service;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserTable saveUser(UserTable user) {
        // Only hash password if it's not already hashed (we'll assume if it starts with $2a$ it's hashed, or we just hash it always on creation)
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        UserTable savedUser = userRepository.save(user);
        
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

    @Override
    public boolean authenticate(String username, String password) {
        Optional<UserTable> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return passwordEncoder.matches(password, user.get().getPassword());
        }
        return false;
    }
}
