package coms.vb1.CyBank.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.SecurityException;
import java.util.Date;

import coms.vb1.CyBank.Tables.*;
import coms.vb1.CyBank.Classes.Report;
import coms.vb1.CyBank.Repos.*;
import coms.vb1.CyBank.Security.*;


@RestController // This means that this class is a Controller
@RequestMapping(path = "/api/user") // This means URL's start with /demo (after Application path)
public class UserAPI {

  @Autowired
  private UserRepo repository; //Repository of all current users

  @Autowired
  private AccountsRepo acctRepo; //Repository of all loans

  @Autowired
  private TransactionsRepo transRepo; //Repository of all loans

  @Autowired
  private LoanRepo loanRepo; //Repository of all current users

  @Autowired
  private loginStateSession login;

  @SecureEndpoint(RestrictBy="Public")
  @GetMapping(path="/Test")
  public ResponseEntity<String> testConn(){

    return HttpUtil.createResponse( "Hit server!", 200 );
  }


    /**
     * Returns all users, optional param, userType
     * @Param userType - A = Admin, S = Support U = User
     */
    
    @SecureEndpoint(RestrictBy="Public")
    @GetMapping(path="/get")
    public @ResponseBody HashMap<String, List<User>> getAllUsers(@RequestParam(required = false) Character userType){

      System.out.println("Account type recieved = " + userType);
      
      HashMap<String, List<User>> responseBody = new HashMap<String, List<User>>();

      if(userType == null){
        responseBody.put("data", repository.findAll());
      } else {
        responseBody.put("data", repository.findByAccountType(userType));
      }



      //This is intended to find all of the accounts associated with a specific uniqueUserId
      return responseBody;
    }

     /**
     * Login to a specific user
     * Will take the password and email out of request body, query the db, then set login bean to this user
     * @Param user - the user to be logged in
     */

    @SecureEndpoint(RestrictBy="Public")
    @PostMapping(path="/login")
    public @ResponseBody User login( @RequestBody User user){

      if(login.login(user)){
        loginState loggedInBean = login.getLoginState();
        return loggedInBean.getCurrentUser();
      }

      throw new SecurityException("Login Failed!");
    }

    /**
     * Add a user to the db
     * @Param user - Takes the requestBody and inserts it into the db
     */

    @SecureEndpoint(RestrictBy="Public")
    @PostMapping(path = "/add")
    public @ResponseBody User addUser(@RequestBody User user) {
      user.setCreatedOn(new Date());
      if (user == null) {
        throw new SecurityException("Could not create user.");
      }
      repository.save(user);
      return user;  
    }

    /**
     * Delete user from db
     * @Param uid - The UniqueUserId of user to be deleted
     */

    //@SecureEndpoint(RestrictBy="User")
    @DeleteMapping(path = "/delete")
    public @ResponseBody HashMap<String, String> deleteUser(@RequestParam Integer uid) {
      User deleteUser = repository.findById(uid).get();
      List<Loans> outstandingLoans = deleteUser.getLoans();
      for(Loans loan : outstandingLoans){
        loanRepo.deleteById(loan.getUniqueLoanId());
      }
      List<Accounts> outstandingAccounts = deleteUser.getAccounts();
      for(Accounts acct : outstandingAccounts){
        List<Transaction> outstandingTransactions = acct.getTransactions();
        for(Transaction trans : outstandingTransactions){
          transRepo.deleteById(trans.getTransactionId());
        }
        acctRepo.deleteById(acct.getUniqueAccountId());
      }

      repository.deleteById(uid);
      HashMap<String, String> responseBody = new HashMap<String, String>();
      responseBody.put("data", "Succcess");
      return responseBody;
    }

    /**
     * Updates user based on the uid passed in
     * @Param user - the user to be updated with the new values
     */

 // @SecureEndpoint(RestrictBy="User")
  @PutMapping(path = "/update")
  public @ResponseBody User updateUser(@RequestBody User user) {
        Optional<User> old = repository.findById(user.getUniqueUserId()); //This may need to change if the user can change the id
        User oldUser = old.get();

        
        if(old == null){ //Check if the old user does not exist
          return null;
        }
        if(user.getIncome() != null){
          oldUser.setIncome(user.getIncome());
        }
        if(user.getSSN() != null){
          oldUser.setSSN(user.getSSN());
        }
        if(user.getAge() != null){
          oldUser.setAge(user.getAge());
        }
        if(user.getName() != null){
          oldUser.setName(user.getName());
        }
        if(user.getEmail() != null){
          oldUser.setEmail(user.getEmail());
        }
        if(user.getPassword() != null){
          oldUser.setPassword(user.getPassword());
        }
        if(user.getAccountType() != null){
          oldUser.setAccountType(user.getAccountType());
        }

        repository.save(oldUser); //Save changes
        return oldUser;
    }

  @GetMapping("/report")
  public @ResponseBody HashMap<String, Object> reportUser(@RequestParam Integer id){
      HashMap<String, Object> report = new HashMap<>();
      User user = repository.findById(id).get();
      Report reportClass = new Report(user);
      report.put("numAccounts", reportClass.getNumAccounts());
      report.put("numTransactions", reportClass.getNumTransactions());
      report.put("numLoans", reportClass.getNumLoans());
      report.put("highestValueAccount", reportClass.getHighestValueAccount());
      report.put("lowestValueAccount", reportClass.getLowestValueAccount());
      report.put("averageAccountBalance", reportClass.getAverageAccountBalance());
      report.put("averageTransactionAmount", reportClass.getAverageTransactionAmount());
      return report;
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