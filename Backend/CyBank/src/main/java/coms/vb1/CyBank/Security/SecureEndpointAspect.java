package coms.vb1.CyBank.Security;

import java.lang.annotation.*;
import coms.vb1.CyBank.Controllers.*;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.*;

import org.springframework.beans.factory.annotation.Autowired;

@Aspect
@Component
public class SecureEndpointAspect {

    @Autowired
    private loginStateSession sessionLogin; // Current wrapper for loginstate bean set at session level



    //Select our annotation
    @Around("@annotation(SecureEndpoint)")
    public Object SecureAnnotatedEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {

        loginState login = sessionLogin.getLoginState();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
    
        SecureEndpoint EndpointAnnotation = method.getAnnotation(SecureEndpoint.class);

        String restrictOn = EndpointAnnotation.RestrictBy();

        boolean continueFunction = true;

        String errMsg = "";
        int errStatus = EndpointAnnotation.StatusOnSuccess();

        if( (login != null && login.getCurrentUser() != null) || restrictOn.equals("Public")){

            switch(restrictOn){
                case "User":
                    Class[] parameters = method.getParameterTypes();
                    int numEndpointParameters = 0;
                    for( Class param : parameters) {

                        // If there is an error here, EndpointParameter class is for some reason undefined
                        if( param.getAnnotation(EndpointParameter.class) != null ){
                            continueFunction &= checkFieldMatchesParemeter(param);
                            numEndpointParameters++;
                        }
                    }

                    if(numEndpointParameters <= 0){
                        errMsg = "No Field name was passed in Paremeter! If the SecureEndpoint Annotation is used with value user, At least one @EndpointParameter should be present in the methods paremeter!";
                        //Internal server error, becuase the client can't do anything about this
                        errStatus = 500;
                    } else if(!login.isValid()){
                        errMsg = "Login Expired! Please login again";
                        //Unathorized
                        errStatus = 401;
                    }
                    break;

                case "Support":

                    // TODO, Return http error response if any of the following checks return true

                    if(login.getCurrentUser().getAccountType() == 'U'){
                        errMsg = "insufficient Permissions, Access Restricted to Support Users and Admins";
                        //Forbidden
                        errStatus = 403;
                    } else if(!login.isValid()){
                        errMsg = "Login Expired! Please login again";
                        //Unathorized
                        errStatus = 401;
                    }
                    break;

                case "Admin":
                    if(login.getCurrentUser().getAccountType() != 'A'){
                        errMsg = "insufficient Permissions, Access Restricted to Admins Only";
                        //Forbidden
                        errStatus = 403;
                    } else if(!login.isValid()){
                        errMsg = "Login Expired! Please login again";
                        //Unathorized
                        errStatus = 401;
                    }
                    break;
                case "Public":
                    //No restrictions on public pages
                break;
                default:
                    if(!login.isValid()){
                        errMsg = "Invalid RestrictBy Parameter! Should be among the following values - 'User' 'Support' 'Admin' 'Public'";
                        //Internal server error, becuase the client can't do anything about this
                        errStatus = 500;
                    }
                    break;

            }
        }  else { // If this is run; login == null;
            errMsg = "Authentication err!";
            //Unathorized
            errStatus = 401;
        }

        //For description on error codes used go to https://developer.mozilla.org/en-US/docs/Web/HTTP/Status 

        //Create http response, if errStatus is an error, return value of the function, otherwise return error.
        //Api's using this should always have a return type of ResponseEntity

        if(errStatus >= 300){

            switch(errStatus){ // We don't user break; in this switch statement becuase when we throw an exception we leave
                               // Adding break will cause an unreachable statement exception

                case 403: //Unauthorized User
                case 401: //User not logged in and is attempting to access restricted page
                    throw new SecurityException(errMsg);
                case 500: // This SecureEndpointAnnotation is being used incorrectly
                    throw new UnsupportedOperationException(errMsg);
                default: // I have no idea how you got here
                    throw new UnsupportedOperationException( "Oh no, Something is terribly wrong here... <SecureEndpointAspect>" );
            }
        }
        
        return joinPoint.proceed();

    }

    private boolean checkFieldMatchesParemeter(Class val){

        loginState login = sessionLogin.getLoginState();

        try {

            System.out.println(val.getName());

            //This is the field in the loginStateClass that we want to match against
            Field referedField = login.getClass().getDeclaredField( val.getName() );


            return referedField.get(login.getClass()).equals((Object)val); // Cast field of login state to object and compare to value of paremeter passed in
            
            

        } catch ( NoSuchFieldException e ) {
            System.err.println( "Cannot find field with name '" + val.getName() + "' in loginState bean" );
            e.printStackTrace();
        } catch(Exception e){
            System.err.println("Unknown Error in file SecureEndpointAspect.java");
            e.printStackTrace();
        }

        return false;


    }

}