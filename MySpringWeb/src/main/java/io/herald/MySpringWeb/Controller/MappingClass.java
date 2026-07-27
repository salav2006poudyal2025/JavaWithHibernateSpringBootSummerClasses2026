package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller  //handles HTTP requests: GET, POST, etc.
public class MappingClass {


    @Autowired
    private UserRepository uRepo;

    @GetMapping("/")   //url pattern for mappping
    public String openFirstPage(){
        return "FirstPage";
    }

    //url in this point was fixed
    @GetMapping("/NextPage")
    public String openNextPage(){
        return "NextPage";
    }
    @GetMapping("/login")
    public String login(){
        return "login";

    }

    @PostMapping("/login")
    public String loginPost(HttpServletRequest request, Model m){
        request.getParameter( "username");
        request.getParameter( "password");
        String username = request.getParameter( "username") ;
        String password = request.getParameter( "password");
        //when a from data does post request, httpServletRequest obtains those data as parameters
        // in controller
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        if(uRepo.existsByUsernameAndPassword(username, hashPassword)){
            List<UserTable> totalUsers = uRepo.findAll();
            m.addAttribute("totalUsers", totalUsers);
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            return "Home.html";
        }
        return "login";
    }
    @GetMapping ("/home")
    public String homeGet(Model m)
    {
        m.addAttribute("totalUsers", uRepo.findAll());
        return "Home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        session.invalidate();
        //logout your session
        return "login";
    }

}
