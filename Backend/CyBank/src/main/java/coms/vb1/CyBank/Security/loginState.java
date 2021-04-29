package coms.vb1.CyBank.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import coms.vb1.CyBank.Repos.*;
import coms.vb1.CyBank.Tables.*;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class loginState {

    @Autowired
    UserRepo userRepo;

    private User user;
    private boolean loginValid = false;
    private long lastCheckIn;

    private int loginStateLifeCycle = 10; //Time in minutes

    public boolean login(String email, String password){

        User exampUser = new User();

        exampUser.setEmail(email);
        exampUser.setPassword(password);
        
        return login(exampUser);
    }

    public boolean login(User user){

        System.out.println( "Searching for user - " + user.getEmail());
        System.out.println( "With Password - " + user.getPassword());
        
        ExampleMatcher matcher = ExampleMatcher.matching()
                                                .withIgnoreNullValues()
                                                .withIgnorePaths("createdOn")
                                                .withMatcher("email", exact())
                                                .withMatcher("password", exact());


        Example exampleUser = Example.of(user, matcher);

        Optional<User> foundUser = userRepo.findOne( exampleUser ); // Returns a single Optional Instance

                                                                    //If User exists; foundUser = User;
                                                                    //Else found user is an empty optional instance

        if(!foundUser.isPresent()){ //Check if this is null or not
            loginValid = false;
            return false;
        } // else 

        lastCheckIn = System.currentTimeMillis();
        loginValid = true;
        this.user = foundUser.get();

        return true;
    }


    public User getCurrentUser(){
        return user;
    }

    public void setCurrentUser(User user){
        this.user = user;
    }

    public void checkIn(){


        long currentTime = System.currentTimeMillis();
        long seconds = ( currentTime - lastCheckIn ) / 1000;

        // If more time passed in seconds is greater than expiration time
        if((loginStateLifeCycle * 60 ) < seconds){
            System.out.println("User with email - " + user.getEmail() + " Timed out!");
            loginValid = false;
        } else {
            lastCheckIn = System.currentTimeMillis();
        }


    }

    public boolean isValid(){
        checkIn();
        return this.loginValid;
    }


}