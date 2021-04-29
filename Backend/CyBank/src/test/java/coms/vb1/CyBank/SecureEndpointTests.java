package coms.vb1.CyBank;
// Import Local classes
import coms.vb1.CyBank.Tables.*;
import coms.vb1.CyBank.Repos.*;
import coms.vb1.CyBank.Security.*;
import coms.vb1.CyBank.Controllers.*;

// Import Java libraries
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Example;

// import junit/spring tests
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

// import mockito related
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

//This doubles as a test for UserAPI test
@RunWith(SpringRunner.class)
@WebMvcTest(UserAPI.class)
public class SecureEndpointTests {

	@Autowired
	private MockMvc controller;

	@MockBean
	private loginStateSession loginState;

	@MockBean // note that this repo is also needed in
	private UserRepo userRepo;

	@MockBean
	private AccountsRepo acctRepo; //Repository of all loans

	@MockBean
	private TransactionsRepo transRepo; //Repository of all loans

	@MockBean
	private LoanRepo loanRepo; //Repository of all current users

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	List<User> l;

	static String endpointPrefix = "/api/user/";

	static ObjectMapper objectMapper = new ObjectMapper();

	User bullyUser;

	User client;
	User support;
	User admin;


	String sBullyUser;

	@BeforeEach 
    void init() {

		l = new ArrayList<User>();

		bullyUser = new User(4, "Bullied User", "bully@user.com", "passw0rd", 'U', "123456789", 22);

		try {
			sBullyUser = objectMapper.writeValueAsString(bullyUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Initialize mocked User table

		client = new User(1, "Client User", "client@user.com", "passw0rd", 'C', "123456789", 22);
		support = new User(2, "Support User", "support@user.com", "passw0rd", 'S', "123456789", 22);
		admin = new User(3, "Admin User", "admin@user.com", "passw0rd", 'A', "123456789", 22);

		l.add(client);
		l.add(support);
		l.add(admin);

		// Route user repo functions through the arraylist

        when(userRepo.findAll()).thenReturn(l);
		when(userRepo.save((User)any(User.class)))
			.thenAnswer(x -> {
				  User r = x.getArgument(0);
				  l.add(r);
				  return r;
	  });
	  when(userRepo.findOne((Example)any(Example.class)))
	  		.thenAnswer(x -> {
				 User exampleUser = (User)(((Example)x.getArgument(0)).getProbe());
				 
				 for(int i = 0; i < l.size(); i++){
					  if(l.get(i).getEmail() == exampleUser.getEmail() && l.get(i).getPassword() == exampleUser.getPassword()){
						  return Optional.of(l.get(i));
					  }
				 }
				 return Optional.of(null);
		});
		when(userRepo.findById((Integer)any(Integer.class)))
	  		.thenAnswer(x -> {
				Integer exampleUserId = x.getArgument(0);
				 
				 for(int i = 0; i < l.size(); i++){
					  if( l.get(i).getUniqueUserId().equals(exampleUserId) ){
						  return Optional.of(l.get(i));
					  }
				 }
				 return Optional.of(null);
		});
	//   when(userRepo.delete((User)any(User.class)))
	// 		.thenAnswer(x -> {
	// 			  User r = x.getArgument(0);
	// 			  l.remove(r);
	// 			  return null;
	//   });

    }


	@Test
	public void testEndpoint() throws Exception {

		controller.perform(get(endpointPrefix + "Test"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("text/plain;charset=UTF-8"));
	}

	@Test
	public void getEndpoint() throws Exception {

		loginState.login(admin);

		controller.perform(get(endpointPrefix + "get"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"));
	}

	@Test
	public void loginEndpoint() throws Exception {

		controller.perform(post(endpointPrefix + "login"))
			.andExpect(status().isBadRequest());
	}

	// @Test
	// public void updateEndpoint() throws Exception {

	// 	controller.perform(post(endpointPrefix + "update"))
	// 		.andExpect(status().isBadRequest());
	// }

	@Test
	public void deleteEndpoint() throws Exception {


		userRepo.save(bullyUser);

		loginState.login(admin);

		controller.perform(delete(endpointPrefix + "delete"))
			.andExpect(status().isBadRequest());

		// controller.perform(delete(endpointPrefix + "delete?uid=4"))
		// 	.andExpect(status().isOk())
		// 	.andExpect(content().contentType("application/json"));

	}

	


	public void expectError(int errStatus, String errMsg){

		exceptionRule.expectMessage(errMsg);

		switch(errStatus){ // We don't user break; in this switch statement becuase when we throw an exception we leave
						// Adding break will cause an unreachable statement exception

				case 403: //Unauthorized User
				case 401: //User not logged in and is attempting to access restricted page
					exceptionRule.expect(SecurityException.class);
					break;
				case 500: // This SecureEndpointAnnotation is being used incorrectly
					exceptionRule.expect(UnsupportedOperationException.class);
					break;
				default: // I have no idea how you got here
					exceptionRule.expect(UnsupportedOperationException.class);
					exceptionRule.expectMessage("Oh no, Something is terribly wrong here... <SecureEndpointAspect>");
					break;
		}

	}

}
