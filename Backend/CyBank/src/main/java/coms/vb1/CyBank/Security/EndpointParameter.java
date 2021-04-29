package coms.vb1.CyBank.Security;

//import org.springframework.beans.factory.annotation.Autowired;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)

//This annotation is purely used by the SecureEndpointAspect to get values out get/put requests.
public @interface EndpointParameter {

}