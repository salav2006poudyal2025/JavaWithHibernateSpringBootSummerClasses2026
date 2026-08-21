package io.herald.MySpringWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MySpringWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringWebApplication.class, args);
	}

}
