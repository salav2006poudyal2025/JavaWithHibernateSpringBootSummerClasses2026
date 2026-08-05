package io.herald.MySpringWeb.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.herald.MySpringWeb.Model.ImageTable;
import io.herald.MySpringWeb.Model.ImageTable2;
import io.herald.MySpringWeb.Repository.Image2Repository;
import io.herald.MySpringWeb.Repository.ImageRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Controller
public class GalleryController {

    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private Image2Repository image2Repo;

    @GetMapping("/gallery")
    public String galleryGet(HttpServletRequest req, Model m) {
        HttpSession session= req.getSession();

        if(session.getAttribute("username")==null)
        {
            m.addAttribute("message","You are not logged in");
            return "login";
        }


        return "galleryPage";
    }


    @PostMapping("/gallery")
    public String galleryPost(@RequestParam("image") MultipartFile image, HttpSession session)
    {
try {
    byte[] imgBytes = image.getBytes();
    //We will use Base64 Encoder,
    //We will encode the byte information of file into string
    //To decode , we will again use the Base64 Decoder

    String imgString = Base64.getEncoder().encodeToString(imgBytes);

    ImageTable img = new ImageTable();
   img.setImage(imgString);

   imageRepo.save(img);
}

catch (IOException e)
{
    e.printStackTrace();
}

session.setAttribute("totalImages",imageRepo.findAll());
        return "galleryPage";
    }



    @GetMapping("/gallery2")
    public String gallery2Get(Model m)
    {
        m.addAttribute("cloudImages",image2Repo.findAll());
        return "galleryPage2";
    }

    @PostMapping("/gallery2")
    public String gallery2Post(@RequestParam ("image")MultipartFile image, Model m)
    {
        try {
            Map uploadResult= cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String imgUrl=uploadResult.get("secure_url").toString();

        ImageTable2 img=new ImageTable2();
        img.setImageUrl(imgUrl);
image2Repo.save(img);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

m.addAttribute("cloudImages",image2Repo.findAll());
        return "galleryPage2";
    }


}
