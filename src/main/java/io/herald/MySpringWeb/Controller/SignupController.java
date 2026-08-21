package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//Controller handles http requests and responses
public class SignupController {

    @Autowired
    private UserService userService;


@GetMapping("/signup")
    public String signup() {
    return "signup.html";
}

@PostMapping("/signup")
    public String postSignup(HttpServletRequest request, Model m)
{
String username=request.getParameter("username");
String password=request.getParameter("password");
String email=request.getParameter("email");

UserTable uc = new UserTable();
uc.setUsername(username);
uc.setPassword(password); // Will be hashed in UserService
uc.setEmail(email);

userService.saveUser(uc);

    System.out.println(username);

    //Model ko m bhanne object le message lera gako -> login.html lai
    //message lai attribute bhaninchha model ko bhasa ma

    //m.addAttribute(msgtitle,msg);
    m.addAttribute("signupSuccess","You have successfully signed up! Please Login!");
    return "login.html";
}


}
