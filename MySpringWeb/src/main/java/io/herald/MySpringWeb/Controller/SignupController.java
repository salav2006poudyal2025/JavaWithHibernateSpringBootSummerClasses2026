package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//Controller handles http requests and responses
public class SignupController {

    @Autowired
    //Autowired annotation helps in dependency injection,
    //when autowired is present, all the necessary dependency files are
    //provided to the autowired class
    //Also, new keyword is not required to satisfy the oop rule to
    //create an object.
    private UserRepository uRepo;


@GetMapping("/signup")
    public String signup() {
    return "signup.html";
}

@PostMapping("/signup")
    public String postSignup(HttpServletRequest request, Model m)
{
String username=request.getParameter("username");
String password=request.getParameter("password");

//MD5 Hashing - Crackable
String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());

UserTable uc = new UserTable();
uc.setUsername(username);
uc.setPassword(hashPassword);

uRepo.save(uc);

    System.out.println(username);
    System.out.println(password);

    //Model ko m bhanne object le message lera gako -> login.html lai
    //message lai attribute bhaninchha model ko bhasa ma

    //m.addAttribute(msgtitle,msg);
    m.addAttribute("signupSuccess","You have successfully signed up! Please Login!");
    return "login.html";
}


}
