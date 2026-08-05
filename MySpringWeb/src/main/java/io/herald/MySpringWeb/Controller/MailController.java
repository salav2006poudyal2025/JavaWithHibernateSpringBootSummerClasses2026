package io.herald.MySpringWeb.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailController {

    @GetMapping("/mail")
    public String mailGet(HttpServletRequest request, Model m)
    {
        HttpSession session = request.getSession();

        if(session.getAttribute("username") == null)
        {
            m.addAttribute("message", "You are not logged in");
            return "login";
        }
        return "mailPage";
    }
}
