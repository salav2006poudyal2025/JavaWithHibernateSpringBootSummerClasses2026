package io.herald.MySpringWeb.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(

                "cloud_name","u2rllcvz",
                "api_key","323551934672627",
                "api_secret","IKr2HnclNE_oPFLv8tWsIcvHDVM",
                "secure",true

        ));
    }





}
