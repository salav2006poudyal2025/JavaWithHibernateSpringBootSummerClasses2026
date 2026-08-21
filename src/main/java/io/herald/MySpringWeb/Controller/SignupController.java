package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "signup";
    }

    /**
     * Handles the POST submission from the signup form.
     * Creates a new UserTable entity and delegates to UserService for saving.
     * @param request Provides access to form parameters.
     * @param m The Model to transport success attributes to the login view.
     * @return View name for login upon successful registration.
     */
    @PostMapping("/signup")
    public String postSignup(HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        // Extract raw form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if (username == null || username.isBlank() || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Please complete every sign-up field.");
            return "redirect:/signup";
        }

        if (userService.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("message", "That username is already in use.");
            return "redirect:/signup";
        }

        // Construct entity and populate with form data
        UserTable uc = new UserTable();
        uc.setUsername(username);
        uc.setPassword(password); // Will be hashed securely within UserService implementation
        uc.setEmail(email);

        // Save the new account. Its email is sent asynchronously, so signup can redirect promptly.
        userService.registerUser(uc);

        // Store a one-time message that is displayed after the redirect to login.
        redirectAttributes.addFlashAttribute("signupSuccess", "Account created. Please sign in.");
        
        return "redirect:/login";
    }

}
