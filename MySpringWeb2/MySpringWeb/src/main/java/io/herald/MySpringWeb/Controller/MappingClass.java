package io.herald.MySpringWeb.Controller;

// Import HttpServletRequest to read data sent from HTML forms
import jakarta.servlet.http.HttpServletRequest;

// Spring MVC annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller   // Marks this class as a Spring MVC Controller
public class MappingClass {

    // Opens the first page when user visits: http://localhost:8080/
    @GetMapping("/")
    public String openFirstPage() {
        return "firstPage.html";
    }

    // Opens the next page when user visits: http://localhost:8080/nextPage
    @GetMapping("/nextPage")
    public String openNextPage() {
        return "nextPage.html";
    }

    // Displays the login page
    // URL: http://localhost:8080/login
    @GetMapping("/login")
    public String loginPage() {
        return "loginPage.html";
    }

    // Handles form submission from loginPage.html
    // This method runs when the form method="POST"
    @PostMapping("/login")
    public String loginPost(HttpServletRequest request) {

        // Read username entered by the user
        String username = request.getParameter("username");

        // Read password entered by the user
        String password = request.getParameter("password");

        // Print values in the console (for testing)
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // Check if username and password are both "admin"
        if (username.equals("admin") && password.equals("admin")) {

            // Login successful
            return "homePage.html";

        } else {

            // Login failed
            return "loginPage.html";
        }
    }
}