package io.herald.MySpringWeb.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.herald.MySpringWeb.Model.ImageTable;
import io.herald.MySpringWeb.Model.ImageTable2;
import io.herald.MySpringWeb.Repository.Image2Repository;
import io.herald.MySpringWeb.Repository.ImageRepository;
import io.herald.MySpringWeb.Repository.UserRepository;
import io.herald.MySpringWeb.Model.UserTable;
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

/**
 * Controller managing image gallery features including local DB image storage and external Cloudinary storage.
 */
@Controller
public class GalleryController {

    @Autowired
    private ImageRepository imageRepo;
    
    @Autowired
    private Cloudinary cloudinary;
    
    @Autowired
    private Image2Repository image2Repo;
    
    @Autowired
    private UserRepository userRepo;

    /**
     * Renders the basic gallery page showing locally stored Base64 images.
     * Ensures the user is logged in.
     * @param req The HTTP request containing the session.
     * @param m The Model for passing messages.
     * @return The gallery view or login redirect.
     */
    @GetMapping("/gallery")
    public String galleryGet(HttpServletRequest req, Model m) {
        HttpSession session = req.getSession();

        if(session.getAttribute("username") == null)
        {
            m.addAttribute("message", "You are not logged in");
            return "login";
        }

        return "galleryPage";
    }

    /**
     * Processes file uploads, converts the image to Base64 format, and saves to local database.
     * @param image The uploaded multipart file.
     * @param session The active user session to link the image to the uploader.
     * @return The gallery view after saving.
     */
    @PostMapping("/gallery")
    public String galleryPost(@RequestParam("image") MultipartFile image, HttpSession session)
    {
        try {
            byte[] imgBytes = image.getBytes();
            
            // We encode the byte information of the file into a Base64 string for database storage
            String imgString = Base64.getEncoder().encodeToString(imgBytes);

            ImageTable img = new ImageTable();
            img.setImage(imgString);
            
            // Link the image to the currently logged in user
            String username = (String) session.getAttribute("username");
            if (username != null) {
                userRepo.findByUsername(username).ifPresent(img::setUser);
            }

            imageRepo.save(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.setAttribute("totalImages", imageRepo.findAll());
        return "galleryPage";
    }

    /**
     * Renders the secondary gallery page that fetches images from Cloudinary URLs.
     * @param m The Model to supply image URLs.
     * @return View name for Cloudinary gallery.
     */
    @GetMapping("/gallery2")
    public String gallery2Get(Model m)
    {
        m.addAttribute("cloudImages", image2Repo.findAll());
        return "galleryPage2";
    }

    /**
     * Processes file uploads to the external Cloudinary service and saves the returned secure URL.
     * @param image The uploaded multipart file.
     * @param m The Model to refresh Cloudinary images.
     * @return View name for Cloudinary gallery.
     */
    @PostMapping("/gallery2")
    public String gallery2Post(@RequestParam ("image") MultipartFile image, Model m)
    {
        try {
            // Upload the raw byte stream directly to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            
            // Extract the secure HTTPS url provided by Cloudinary
            String imgUrl = uploadResult.get("secure_url").toString();

            ImageTable2 img = new ImageTable2();
            img.setImageUrl(imgUrl);
            
            // Note: If you want to link the Cloudinary image to the user, you would need the session here as well.
            image2Repo.save(img);
        } catch(IOException e) {
            e.printStackTrace();
        }

        m.addAttribute("cloudImages", image2Repo.findAll());
        return "galleryPage2";
    }

}
