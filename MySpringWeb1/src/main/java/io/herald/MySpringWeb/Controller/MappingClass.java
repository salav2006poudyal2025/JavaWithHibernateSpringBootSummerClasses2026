package io.herald.MySpringWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  //Handles Http Requests : Get , Post , etc
public class MappingClass {

    @GetMapping("/") //url pattern for mapping
    public String openFirstPage()
    {
        return "firstPage.html";
    }

    @GetMapping("/nextPage")
    public String OpenNextPage()
    {
        return "nextPage.html";
    }
@GetMapping("/login")
    public String loginPage()
{
    return "loginPage.html";
}

    @GetMapping("/signup")
    public String signupPage()
    {
        return "signupPage.html";
    }

}
