package io.herald.MySpringWeb.Repository;

import io.herald.MySpringWeb.Model.UserTable;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for the UserTable entity.
 * Provides standard CRUD operations and allows for custom query methods.
 */
@Repository
// Repository -> Uses JPA and Hibernate to connect to our required database and tables
public interface UserRepository extends JpaRepository<UserTable, Integer> {

    // Custom Queries
    
    /**
     * Checks if a user exists by matching exactly the provided username and password.
     * @param un The username.
     * @param pwd The raw password (Note: currently bypassing BCrypt if used directly).
     * @return true if exists, false otherwise.
     */
    boolean existsByUsernameAndPassword(String un, String pwd);
    
    /**
     * Retrieves a user wrapped in an Optional based on their username.
     * @param username The exact username.
     * @return Optional UserTable.
     */
    java.util.Optional<UserTable> findByUsername(String username);
}
