package coms.vb1.CyBank.Security;

//import org.springframework.beans.factory.annotation.Autowired;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecureEndpoint {

    public String RestrictBy() default "";
    public int StatusOnSuccess() default 200;

}