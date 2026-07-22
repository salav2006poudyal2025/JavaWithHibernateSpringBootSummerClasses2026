package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {
    @Autowired
    //Autowired annotation helps in dependency injection
    //when autowired is present, all the necessary dependency files are provided to the autowired class
    //also, new keyword is not required to satisfy the oop rule to create an object
    private UserRepository uRepo;

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

        //MDS Hashing - Crackable
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        UserTable uc = new UserTable();
        uc.setUsername(username);
        uc.setPassword(password);

        uRepo.save(uc);
        System.out.println(username);
        System.out.println(password);

        //model ko m vnane object le message liyera gako -> login.html lai
        //message lai attribute vaninxa model ko vasa ma

        // m.attribute(msg title, message)
        m.addAttribute("SignupSuccess", "You have successfully signed up");
        // After successful signup, redirect to login page
        return "loginPage.html";
    }
}