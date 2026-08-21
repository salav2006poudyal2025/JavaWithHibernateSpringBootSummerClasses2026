package io.herald.MySpringWeb.Service;

import io.herald.MySpringWeb.Model.UserTable;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserTable saveUser(UserTable user);
    Optional<UserTable> findById(int id);
    Optional<UserTable> findByUsername(String username);
    List<UserTable> findAllUsers();
    void deleteUser(int id);
    boolean authenticate(String username, String password);
}
