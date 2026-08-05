package io.herald.MySpringWeb.Controller;

import io.herald.MySpringWeb.Model.UserTable;
import io.herald.MySpringWeb.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
private UserRepository userRepository;

    @PostMapping("deleteUser")
    public String deleteUser(@RequestParam("id") int id , Model m)
    {
        userRepository.deleteById(id);
m.addAttribute("totalUsers",userRepository.findAll());

        return "home";
    }


    @PostMapping("/editUser")
    public String editUser(@RequestParam ("id") int id, Model m)
    {
        Optional<UserTable> ut= userRepository.findById(id);
//Optional helps to check if there is a required object or not.
        if(ut.isPresent())
        {
            UserTable user=ut.get();
m.addAttribute("user",user);
return "editPage";
        }

        m.addAttribute("totalUsers",userRepository.findAll());
        return "home";
    }



    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute UserTable user, Model m)
    {
        //@ModelAttribute - if the parameters incoming are the same as your
        //model table, use the following syntax for its parameters:
        //instead of @RequestParam, we can directly use:
        //@ModelAttribute name_of_table obj_name

        userRepository.save(user);

        m.addAttribute("totalUsers",userRepository.findAll());
        return "home";
    }
}
