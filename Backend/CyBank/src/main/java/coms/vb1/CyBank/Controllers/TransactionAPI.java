package coms.vb1.CyBank.Controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.lang.SecurityException;

import coms.vb1.CyBank.Repos.*;
import coms.vb1.CyBank.Tables.*;

@RestController
@RequestMapping(path = "/api/transactions")
public class TransactionAPI {

    @Autowired
    private UserRepo usrRep; //Repository of all current users

    @Autowired
    public TransactionsRepo tranRepo;

    @Autowired
    private AccountsRepo accRepo;

    /**
     * Return all available transactions
     */

    @GetMapping("/get")
    public @ResponseBody Iterable<Transaction> findAllTransactions(){
        return tranRepo.findAll();
    }

    /**
     * Add a new transaction to a specific account, we get the user associated with the account and add needed database entries
     * @Param transaction - The transaction to be added
     * @Param uniqueAccountId - The Id of the account where we are adding the transaction
     */

    @PostMapping("/add/{uniqueAccountId}")
    public @ResponseBody HashMap<String, List<Transaction>> addTransaction(@RequestBody Transaction transaction, @PathVariable Integer uniqueAccountId){


        Accounts foundAccount = accRepo.findById(uniqueAccountId).get();

        HashMap<String, List<Transaction>> responseBody = new HashMap<String, List<Transaction>>();

        if(foundAccount == null){
            throw new SecurityException("Could not find Account!");
        }

        User foundUser = foundAccount.getUser();

        if(foundUser == null){
            throw new SecurityException("Could not find User!");
        }

        if(transaction == null){
            throw new UnsupportedOperationException("You must pass in an account to add!");
        }

        foundAccount.setUser(foundUser);

        transaction.setAccount(foundAccount);
        transaction.setUser(foundUser);

        tranRepo.save(transaction);

        responseBody.put("data", foundAccount.getTransactions());

        return responseBody;
    }

    /**
     * Update transaction
     * @Param transaction - The transaction to be updated, based on id
     */
    @PutMapping("/update")
    public @ResponseBody Transaction updateTransaction(@RequestBody Transaction transaction){
        Optional<Transaction> old = tranRepo.findById(transaction.getTransactionId());
        Transaction oldRow = old.get();
        if(!old.isPresent()){
            throw new UnsupportedOperationException("Could not find user user with the account id - " + transaction.getTransactionId() );
        }
        if(transaction.getAmount() != null){
            oldRow.setAmount(transaction.getAmount());
        }
        if(transaction.getDescription() != null){
            oldRow.setDescription(transaction.getDescription());
        }
        if(transaction.getTransactionType() != null){
            oldRow.setTransactionType(transaction.getTransactionType());
        }
        tranRepo.save(oldRow);
        return oldRow;
    }

    /**
     * Delete transaction
     * @Param tid - The unique id of the transaction to be deleted
     */

    @DeleteMapping("/delete")
    public @ResponseBody List<Transaction> deleteTransaction(@RequestParam int tid){
        tranRepo.deleteById(tid);
        return tranRepo.findAll();
    }

    /**
     * Find all transactions based on the account id passed
     * @Param accountId - The account that we want the transactions from
     */

     
    
    @GetMapping("/{uniqueUserId}")
    public @ResponseBody HashMap<String, List<Transaction>> findAccountsByUserId(@PathVariable int uniqueUserId){

        Optional<User> resp = usrRep.findById(uniqueUserId);

        if (!resp.isPresent()){
            throw new SecurityException("User not found!");
        }

        User dataResponse = resp.get();

        List<Transaction> allTransactions = new ArrayList<Transaction>();

        for( Accounts account : dataResponse.getAccounts()){
            allTransactions.addAll(account.getTransactions());
        }

        HashMap<String, List<Transaction>> responseBody = new HashMap<String, List<Transaction>>();

        responseBody.put("data", allTransactions);

        return responseBody;
    }
    @GetMapping("/getByAid/{accountId}")
    public @ResponseBody List<Transaction> findTransactionByAccount(@PathVariable Integer accountId){

        Accounts foundAccount = accRepo.findById(accountId).get();

        if(foundAccount == null){
            throw new SecurityException("Could not find Account!");
        }

        return foundAccount.getTransactions();
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