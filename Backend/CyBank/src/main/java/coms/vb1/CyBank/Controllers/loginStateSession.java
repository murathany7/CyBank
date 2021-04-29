package coms.vb1.CyBank.Controllers;

import coms.vb1.CyBank.Tables.*;
import coms.vb1.CyBank.Security.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class loginStateSession {

	@Autowired
	private loginState state;

    public boolean login(String email, String password){
        return state.login(email, password);
    }

    public boolean login(User user){
        return state.login(user);
    }

	public loginState getLoginState() {
		return state;
	}


}