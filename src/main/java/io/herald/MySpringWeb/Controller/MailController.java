package io.herald.MySpringWeb.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling mail-related frontend views.
 */
@Controller
public class MailController {

    /**
     * Handles GET requests to display the mail page.
     * Checks if the user is authenticated via session before allowing access.
     * @param request The HTTP request to retrieve the session.
     * @param m The Model to pass attributes to the view.
     * @return The view name (mailPage) if authenticated, else redirects to login.
     */
    @GetMapping("/mail")
    public String mailGet(HttpServletRequest request, Model m)
    {
        HttpSession session = request.getSession();

        // Protect the route by checking session existence
        if(session.getAttribute("username") == null)
        {
            m.addAttribute("message", "You are not logged in");
            return "login";
        }
        
        return "mailPage";
    }
}
