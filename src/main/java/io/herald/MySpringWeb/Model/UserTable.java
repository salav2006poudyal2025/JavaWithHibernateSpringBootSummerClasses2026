package io.herald.MySpringWeb.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;

/**
 * JPA Entity representing the 'users' table in the database.
 * Holds authentication credentials, user details, and relationships to uploaded images.
 */
@Entity
@Data // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode
public class UserTable {

    // Primary Key, auto-incremented by the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    // User's display name or login handle
    @NotBlank(message = "Username is required")
    private String username;
    
    // User's contact email, validated for proper format
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    // Encrypted password hash (BCrypt)
    @NotBlank(message = "Password is required")
    private String password;

    // One user can have many locally stored images. 
    // CascadeType.ALL ensures that if a user is deleted, their images are also deleted.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ImageTable> images;

    // One user can have many Cloudinary stored images.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ImageTable2> cloudImages;

    // Explicit getters and setters are technically redundant due to @Data,
    // but they can be kept if they are explicitly relied upon by certain frameworks.
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
