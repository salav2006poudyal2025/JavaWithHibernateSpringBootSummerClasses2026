package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller handling user-specific management actions like deleting, editing, and updating profiles.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles the deletion of a user profile.
     * @param id The ID of the user to delete, passed via form parameters.
     * @param m The Model to refresh the user list on the home view.
     * @return Redirects back to the home page view.
     */
    @PostMapping("deleteUser")
    public String deleteUser(@RequestParam("id") int id , Model m)
    {
        // Execute deletion
        userService.deleteUser(id);
        
        // Refresh users list for the table rendering
        m.addAttribute("totalUsers", userService.findAllUsers());

        return "home";
    }

    /**
     * Handles the request to edit a specific user.
     * Retrieves user details and routes to the edit view.
     * @param id The ID of the user to edit.
     * @param m The Model to transport user details.
     * @return The edit view if found, else falls back to home view.
     */
    @PostMapping("/editUser")
    public String editUser(@RequestParam ("id") int id, Model m)
    {
        // Wrap retrieval in Optional for null-safety
        Optional<UserTable> ut = userService.findById(id);

        if(ut.isPresent())
        {
            UserTable user = ut.get();
            m.addAttribute("user", user);
            return "editPage";
        }

        // If not found, fall back to home with the standard list
        m.addAttribute("totalUsers", userService.findAllUsers());
        return "home";
    }

    /**
     * Handles the submission of the user edit form.
     * Uses ModelAttribute to dynamically bind form fields to a UserTable entity.
     * @param user Bound user entity containing updated properties.
     * @param m The Model to refresh the user list.
     * @return Returns to the home view with the updated lists.
     */
    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("id") int id,
                             @RequestParam("username") String username,
                             @RequestParam("email") String email,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes)
    {
        if (username == null || username.isBlank() || email == null || email.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Username and email are required.");
            return "redirect:/home";
        }

        Optional<UserTable> existingUser = userService.findById(id);
        if (existingUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "User not found.");
            return "redirect:/home";
        }

        // Start with the saved entity. This keeps its BCrypt password and image relationships intact.
        UserTable user = existingUser.get();
        String oldUsername = user.getUsername();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        userService.saveUser(user);

        // Keep the active browser session in sync when the signed-in user changes their name.
        HttpSession session = request.getSession(false);
        if (session != null && oldUsername.equals(session.getAttribute("username"))) {
            session.setAttribute("username", user.getUsername());
        }

        redirectAttributes.addFlashAttribute("message", "User details updated successfully.");
        return "redirect:/home";
    }
}
