package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller handling user registration and signup flows via frontend forms.
 */
@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    /**
     * Displays the signup HTML page.
     * @return View name for signup.
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup.html";
    }

    /**
     * Handles the POST submission from the signup form.
     * Creates a new UserTable entity and delegates to UserService for saving.
     * @param request Provides access to form parameters.
     * @param m The Model to transport success attributes to the login view.
     * @return View name for login upon successful registration.
     */
    @PostMapping("/signup")
    public String postSignup(HttpServletRequest request, Model m)
    {
        // Extract raw form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // Construct entity and populate with form data
        UserTable uc = new UserTable();
        uc.setUsername(username);
        uc.setPassword(password); // Will be hashed securely within UserService implementation
        uc.setEmail(email);

        // Save the newly registered user to the database
        userService.saveUser(uc);

        System.out.println("Registered new user: " + username);

        // Add a success message to display on the login page after routing
        m.addAttribute("signupSuccess", "You have successfully signed up! Please Login!");
        
        // Return to the login view so the user can authenticate
        return "login.html";
    }

}
