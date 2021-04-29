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
@WebMvcTest(LoanAPI.class)
public class LoansAPITests {

    @MockBean
    private UserRepo userRepo;

    @MockBean 
    private LoanRepo loanRepo;

    @Autowired
    private MockMvc controller;

    static String endpointPrefix = "/api/loans/";

    static ObjectMapper objectMapper = new ObjectMapper();


    List<Loans> loanList;

    Loans dummyLoan;

    String sDummyLoan;

    @BeforeEach
    void init(){
        //Initialize lists
        loanList = new ArrayList<Loans>();

        //Set up user to tie to accounts
        User client = new User(1, "Client User", "client@user.com", "passw0rd", 'C', "123456789", 22);
		User support = new User(2, "Support User", "support@user.com", "passw0rd", 'S', "123456789", 22);
		User admin = new User(3, "Admin User", "admin@user.com", "passw0rd", 'A', "123456789", 22);


        //Setup transactions list
        Loans loan1 = new Loans(client, 1, 400.0, 200.0, 10.0, 0.7);
        Loans loan2 = new Loans(support, 2, 1000.0, 900.0, 100.0, 0.015);
        Loans loan3 = new Loans(admin, 3, 100.0, 50.0, 1.0, 0.8);
        loanList.add(loan1);
        loanList.add(loan2);
        loanList.add(loan3);

        //Set dummy transaction for additional use
        dummyLoan = new Loans(client, 4, 20000.0, 15000.0, 350.0, 0.01);

        try{
            sDummyLoan = objectMapper.writeValueAsString(dummyLoan);
        }catch(Exception e){
            e.printStackTrace();
        }

        when(loanRepo.findAll()).thenReturn(loanList);
		when(loanRepo.save((Loans)any(Loans.class)))
			.thenAnswer(x -> {
				  Loans r = x.getArgument(0);
				  for(int i = 0; i < loanList.size(); ++i){
					  if(loanList.get(i).getUniqueLoanId() == r.getUniqueLoanId()){
						loanList.remove(i);
						break;
					  }
				  }
				  loanList.add(r);
				  return r;
	  });
		when(loanRepo.findById((Integer)any(Integer.class)))
	  		.thenAnswer(x -> {
				Integer exampleLoanId = x.getArgument(0);
				 
				 for(int i = 0; i < loanList.size(); i++){
					  if( loanList.get(i).getUniqueLoanId().equals(exampleLoanId) ){
						  return Optional.of(loanList.get(i));
					  }
				 }
				 return Optional.of(null);
		});
		doAnswer(x -> {
			Integer i = x.getArgument(0);
			Loans r = loanRepo.findById(i).get();
			loanList.remove(r);
			return null;
		}).when(loanRepo).deleteById((Integer)any(Integer.class));
    }

    @Test
    public void TestAddLoan(){
        try{
            controller.perform(post(endpointPrefix + "add/1").content(sDummyLoan).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
            assert(loanRepo.findById(4) != null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
		
        
    }

    @Test
    public void TestDeleteLoan(){
        
        try {
            controller.perform(post(endpointPrefix + "add/4").content(sDummyLoan).contentType(MediaType.APPLICATION_JSON))
            	.andExpect(status().isOk())
            	.andExpect(content().contentType("application/json"));
            controller.perform(delete(endpointPrefix + "delete").param("uniqueLoanId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

             assertFalse(loanList.contains(dummyLoan));            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
