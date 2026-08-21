import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws Exception {
        for (Method m : HttpSecurity.class.getMethods()) {
            if (m.getName().equals("authorizeHttpRequests") || m.getName().equals("authorizeRequests")) {
                System.out.println(m);
            }
        }
    }
}
