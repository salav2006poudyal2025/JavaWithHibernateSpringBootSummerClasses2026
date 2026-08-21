package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Main Controller handling general application routing including home, login, and static pages.
 */
@Controller  // Handles HTTP Requests : GET, POST, etc.
public class MappingClass {

    @Autowired
    private UserService userService;

    /**
     * Renders the initial landing page.
     * @return View name for the first page.
     */
    @GetMapping("/")
    public String openFirstPage()
    {
        return "firstPage";
    }

    /**
     * Routes to an auxiliary test page.
     * @return View name for next page.
     */
    @GetMapping("/nextPage")
    public String OpenNextPage()
    {
        return "nextPage";
    }

    /**
     * Displays the login form page.
     * @return View name for login page.
     */
    @GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }

    /**
     * Processes login authentication credentials.
     * @param request The HTTP request to extract parameters and manage session.
     * @param m The Model to transport data to the home view upon success.
     * @return View name for home if successful, else reloads login.
     */
    @PostMapping("/login")
    public String loginPost(HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        // When form data submits a POST request, HttpServletRequest obtains those data as parameters.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Please enter both your username and password.");
            return "redirect:/login";
        }

        // Authenticate credentials via the service layer
        if(userService.authenticate(username, password))
        {
            // Establish the user session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            
            return "redirect:/home";
        }

        // Authentication failed, redirect so the browser has a clean login URL.
        redirectAttributes.addFlashAttribute("message", "Invalid username or password.");
        return "redirect:/login";
    }

    /**
     * Renders the home dashboard view.
     * @param m The Model to supply the user listing.
     * @return View name for home.
     */
    @GetMapping("/home")
    public String homeGet(Model m)
    {
        m.addAttribute("totalUsers", userService.findAllUsers());
        return "home";
    }

    /**
     * Invalidates the current user session and logs the user out.
     * @param request The HTTP request containing the active session.
     * @return Redirects back to the login view.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destroys the session securely
        }

        return "redirect:/login";
    }

}
