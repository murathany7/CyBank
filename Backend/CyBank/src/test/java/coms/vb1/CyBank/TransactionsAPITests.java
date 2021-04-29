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
@WebMvcTest(TransactionAPI.class)
public class TransactionsAPITests {

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private AccountsRepo accRepo;

    @MockBean 
    private TransactionsRepo transRepo;

    @Autowired
    private MockMvc controller;

    static String endpointPrefix = "/api/transactions/";

    static ObjectMapper objectMapper = new ObjectMapper();

    List<Accounts> accList;

    List<Transaction> transList;

    Transaction dummyTransaction;

    String sDummyTransaction;

    @BeforeEach
    void init(){
        //Initialize lists
        accList = new ArrayList<Accounts>();
        transList = new ArrayList<Transaction>();

        //Set up user to tie to accounts
        User client = new User(1, "Client User", "client@user.com", "passw0rd", 'C', "123456789", 22);
		User support = new User(2, "Support User", "support@user.com", "passw0rd", 'S', "123456789", 22);
		User admin = new User(3, "Admin User", "admin@user.com", "passw0rd", 'A', "123456789", 22);

        //Setup accounts list
        Accounts acc1 = new Accounts(1, client, 1, "Client", 'C');
        Accounts acc2 = new Accounts(2, support, 2, "Support", 'S');
        Accounts acc3 = new Accounts(3, admin, 3, "Admin", 'C');
        accList.add(acc1);
        accList.add(acc2);
        accList.add(acc3);

        //Setup transactions list
        Transaction tran1 = new Transaction(acc1, 1, 30.00, "Basic transaction", 1);
        Transaction tran2 = new Transaction(acc1, 2, 50.00, "Paycheck", 2);
        Transaction tran3 = new Transaction(acc1, 3, 70.00, "Gift", 3);
        transList.add(tran1);
        transList.add(tran2);
        transList.add(tran3);

        //Set dummy transaction for additional use
        dummyTransaction = new Transaction(acc1, 4, 90.00, "Dummy", 4);

        try{
            sDummyTransaction = objectMapper.writeValueAsString(dummyTransaction);
        }catch(Exception e){
            e.printStackTrace();
        }

        when(transRepo.findAll()).thenReturn(transList);
        when(accRepo.findAll()).thenReturn(accList);
		when(transRepo.save((Transaction)any(Transaction.class)))
			.thenAnswer(x -> {
				  Transaction r = x.getArgument(0);
				  for(int i = 0; i < transList.size(); ++i){
					  if(transList.get(i).getTransactionId() == r.getTransactionId()){
						transList.remove(i);
						break;
					  }
				  }
				  transList.add(r);
				  return r;
	  });
	  when(transRepo.findOne((Example)any(Example.class)))
	  		.thenAnswer(x -> {
				 Transaction exampleTransaction = (Transaction)(((Example)x.getArgument(0)).getProbe());
				 
				 for(int i = 0; i < transList.size(); i++){
					  if(transList.get(i).getDescription() == exampleTransaction.getDescription()){
						  return Optional.of(transList.get(i));
					  }
				 }
				 return Optional.of(null);
		});
		when(transRepo.findById((Integer)any(Integer.class)))
	  		.thenAnswer(x -> {
				Integer exampleTransactionId = x.getArgument(0);
				 
				 for(int i = 0; i < transList.size(); i++){
					  if( transList.get(i).getTransactionId().equals(exampleTransactionId) ){
						  return Optional.of(transList.get(i));
					  }
				 }
				 return Optional.of(null);
		});
		doAnswer(x -> {
			Integer i = x.getArgument(0);
			Transaction r = transRepo.findById(i).get();
			transList.remove(r);
			return null;
		}).when(transRepo).deleteById((Integer)any(Integer.class));
    }

    @Test
    public void TestAddTransaction(){
        try{
            controller.perform(post(endpointPrefix + "add/4").content(sDummyTransaction).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
            assert(transRepo.findById(4) != null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
		
        
    }

    @Test
    public void TestDeleteTransaction(){
        
        try {
            controller.perform(post(endpointPrefix + "add/4").content(sDummyTransaction).contentType(MediaType.APPLICATION_JSON))
            	.andExpect(status().isOk())
            	.andExpect(content().contentType("application/json"));
            controller.perform(delete(endpointPrefix + "delete").param("tid", "4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

             assertFalse(transList.contains(dummyTransaction));            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
