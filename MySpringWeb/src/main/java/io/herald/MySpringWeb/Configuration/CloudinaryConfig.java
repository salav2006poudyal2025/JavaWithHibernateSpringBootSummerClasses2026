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

                "cloud_name","qw4y79pc",
                "api_key","319832791335966",
                "api_secret","cnNTNDCjxYdzns5geKmvPAN4Ggo",
                "secure",true

        ));
    }





}
