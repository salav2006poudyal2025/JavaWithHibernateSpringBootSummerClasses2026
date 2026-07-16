package io.herald.MySpringWeb.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    // Displays the signup page
    @GetMapping("/signup")
    public String signup() {
        return "signupPage.html";
    }

    // Handles signup form submission
    @PostMapping("/signup")
    public String postSignup(HttpServletRequest request) {

        // Read values sent from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Print values in the console
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // After successful signup, redirect to login page
        return "loginPage.html";
    }
}