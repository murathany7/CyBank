package coms.vb1.CyBank.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import coms.vb1.CyBank.Repos.*;
import coms.vb1.CyBank.Tables.*;
import coms.vb1.CyBank.Security.*;

@RestController
@RequestMapping(path = "/api/accounts/")
public class AccountsAPI {

    @Autowired
    private UserRepo userRep;

    @Autowired
    private AccountsRepo accRepo;

    @Autowired
    private TransactionsRepo tranRepo;

    @Autowired
    private loginStateSession login;

    /**
     * Get all accounts available in the db
     */

    @SecureEndpoint(RestrictBy="Support")
    @GetMapping("/get")
    public @ResponseBody Iterable<Accounts> findAllAccounts() {
        return accRepo.findAll();
    }

    /**
     * Find all accounts associated with a user
     * @Param uniqueUserId - The user to get the accounts from
     */

    //@SecureEndpoint(RestrictBy="User")
    @GetMapping("/{uniqueUserId}")
    public @ResponseBody HashMap<String, List<Accounts>> findAccountsByUserId(@PathVariable /* @EndpointParameter */ int uniqueUserId){

        HashMap<String, List<Accounts>> responseBody = new HashMap<String, List<Accounts>>();

        List<Accounts> userAccounts; 

        try {

            userAccounts = userRep.findById(uniqueUserId).get().getAccounts();

        } catch (Exception e ){

            throw new UnsupportedOperationException("Could not find User!");

        }

        responseBody.put("data", userAccounts);

        //This is intended to find all of the accounts associated with a specific uniqueUserId
        return responseBody;
    }

    /**
     * Add a new account with initial balance
     * @Param postJson - A json with the uniqueUserId, accountName, and accountType to be created
     * @Param initialBalance - The value of the first transaction to be made on the account
     */

    @PostMapping("/add")
    public @ResponseBody HashMap<String, List<Accounts>> addAccount(@RequestBody HashMap<String, String> postJson, @RequestParam Double initialBalance){
        
        if(postJson == null){
            throw new UnsupportedOperationException("No new account found");
        }

        HashMap<String, List<Accounts>> responseBody = new HashMap<String, List<Accounts>>();

        List<Accounts> userAccounts;

        User currentUser;

        loginState loggedInBean = login.getLoginState();
        currentUser = loggedInBean.getCurrentUser();

        try {

            Integer uniqueUserId = Integer.parseInt(postJson.get("uniqueUserId"));

            System.out.println("Add account to user with user id = " + uniqueUserId);

            currentUser = userRep.findById(uniqueUserId).get();


        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not find user");
        }

        Accounts acct = new Accounts();

        try {

            String accountName = postJson.get("accountName");
            Character accountType = postJson.get("accountType").charAt(0);

            acct.setAccountName(accountName);
            acct.setAccountType(accountType);

        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not create account");
        }

        acct.setUser(currentUser);

        accRepo.save(acct);

        ExampleMatcher matcher = ExampleMatcher.matching()
                                                .withIgnoreNullValues()
                                                .withMatcher("uniqueUserId", exact())
                                                .withMatcher("accountName", exact());


        Example exampleAccount = Example.of(acct, matcher);

        Optional<Accounts> foundAccount = accRepo.findOne( exampleAccount );
        Accounts fAccount = foundAccount.get();
        

        List<Transaction> tranList = new ArrayList<Transaction>();
        Transaction firstTransaction = new Transaction();

        firstTransaction.setAmount(initialBalance);
        firstTransaction.setAccount(fAccount);
        firstTransaction.setDescription("Initial Balance");
        firstTransaction.setUser(fAccount.getUser());

        tranRepo.save(firstTransaction);
        fAccount.addTransaction(firstTransaction);


        responseBody.put("data", fAccount.getUser().getAccounts());
        

        return responseBody;
    }

    /**
     * Update account
     * @Param account - The accouont to be updated with the new values of the account
     */

    @PutMapping("/update")
    public @ResponseBody Accounts updateAccount(@RequestBody Accounts account){
        if(!accRepo.existsById(account.getUniqueAccountId())){
            throw new UnsupportedOperationException("Could not find account");
        }
        accRepo.save(account);
        return accRepo.findById(account.getUniqueAccountId()).get();
    }

    /**
     * Delete account
     * @Param aid - The unique account id of the account to be deleted
     */

    @DeleteMapping("/delete")
    public @ResponseBody HashMap<String, String> deleteAccount(@RequestParam Integer aid){

        Accounts foundAccount;
        try {
            foundAccount = accRepo.findById(aid).get();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not find User!");
        }

        List<Transaction> transactions = foundAccount.getTransactions();

        for(int i = 0; i < transactions.size(); i++){
            tranRepo.deleteById(transactions.get(i).getTransactionId());
        }

        accRepo.deleteById(aid);
        HashMap<String, String> responseBody = new HashMap<String, String>();
        responseBody.put("data", "Succcess");
        return responseBody;
    }

    // -----------   Exception handlers, Spring boot will call these if any of the above methods return an exception specified in the "ExceptionHandler" annotation

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handeleSecurityException(
        SecurityException exception
    ) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(exception.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handeleUnsupportedOperationException(
        UnsupportedOperationException exception
    ) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(exception.getMessage());
    }

}
