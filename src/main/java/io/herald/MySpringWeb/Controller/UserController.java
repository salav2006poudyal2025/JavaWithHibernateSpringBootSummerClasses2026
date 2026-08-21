package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String updateUser(@ModelAttribute UserTable user, Model m)
    {
        // Overwrites the existing user data based on the bound primary key (ID)
        userService.saveUser(user);

        // Refresh users list for the table rendering
        m.addAttribute("totalUsers", userService.findAllUsers());
        
        return "home";
    }
}
