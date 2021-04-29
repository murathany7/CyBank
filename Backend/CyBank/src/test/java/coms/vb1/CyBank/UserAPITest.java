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
import org.assertj.core.internal.bytebuddy.implementation.bind.annotation.Empty;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

// import mockito related
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;

//This doubles as a test for UserAPI test
@RunWith(SpringRunner.class)
@WebMvcTest(UserAPI.class)

public class UserAPITest {

    @MockBean
    private loginStateSession login;

    @MockBean
    private UserRepo userRepo;

    @Autowired
	private MockMvc controller;

	@MockBean
	private AccountsRepo acctRepo; //Repository of all loans

	@MockBean
	private TransactionsRepo transRepo; //Repository of all loans

	@MockBean
	private LoanRepo loanRepo; //Repository of all current users

    
    List<User> l;

	static String endpointPrefix = "/api/user/";

	static ObjectMapper objectMapper = new ObjectMapper();

	User bullyUser;

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

		User client = new User(1, "Client User", "client@user.com", "passw0rd", 'C', "123456789", 22);
		User support = new User(2, "Support User", "support@user.com", "passw0rd", 'S', "123456789", 22);
		User admin = new User(3, "Admin User", "admin@user.com", "passw0rd", 'A', "123456789", 22);

		l.add(client);
		l.add(support);
		l.add(admin);

		// Route user repo functions through the arraylist

        when(userRepo.findAll()).thenReturn(l);
		when(userRepo.save((User)any(User.class)))
			.thenAnswer(x -> {
				  User r = x.getArgument(0);
				  for(int i = 0; i < l.size(); ++i){
					  if(l.get(i).getUniqueUserId() == r.getUniqueUserId()){
						l.remove(i);
						break;
					  }
				  }
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
		doAnswer(x -> {
			Integer i = x.getArgument(0);
			User r = userRepo.findById(i).get();
			l.remove(r);
			return null;
		}).when(userRepo).deleteById((Integer)any(Integer.class));

		doAnswer(x -> {
			return null;
		}).when(acctRepo).deleteById((Integer)any(Integer.class));

		doAnswer(x -> {
			return null;
		}).when(transRepo).deleteById((Integer)any(Integer.class));

		doAnswer(x -> {
			return null;
		}).when(loanRepo).deleteById((Integer)any(Integer.class));

	}

    // @Test
    // public void testGetAllUsers(){
    // }

    @Test
    public void TestAddUser() throws Exception {
        
		controller.perform(post(endpointPrefix + "add").content(sBullyUser).contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType("application/json"));


        for(int i = 0; i < l.size(); i++){
            System.out.println(l.get(i).getUniqueUserId());
        }
        assert(userRepo.findById(4) != null);

    }

    @Test
    public void TestDeleteUser() throws Exception {
		controller.perform(post(endpointPrefix + "add").content(sBullyUser).contentType(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType("application/json"));

        // controller.perform(delete(endpointPrefix + "delete?uid=4"))
        // 	.andExpect(status().isOk())
        // 	.andExpect(content().contentType("application/json"));
        
		assertFalse(l.contains(bullyUser));
    }

	@Test
	public void TestUpdateUser() throws Exception {

		controller.perform(post(endpointPrefix + "add").content(sBullyUser).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"));
		
		bullyUser.setName("Bullied Usersss");

		String sBullyUser2 = objectMapper.writeValueAsString(bullyUser);

		controller.perform(put(endpointPrefix + "update").content(sBullyUser2).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"));

		// assertEquals("Bullied Usersss", userRepo.findById(4).get().getName(), "User not updated");
	}

}
