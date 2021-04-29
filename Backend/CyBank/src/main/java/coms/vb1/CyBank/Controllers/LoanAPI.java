package coms.vb1.CyBank.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import coms.vb1.CyBank.Repos.*;
import coms.vb1.CyBank.Tables.*;

@RestController
@RequestMapping(path = "/api/loans")
public class LoanAPI {

  @Autowired
  LoanRepo loanRepo;

  @Autowired
  UserRepo usrRep; // Repository of all current users

  /**
   * Returns all available loans from the db
   */

  @GetMapping("/get")
  public @ResponseBody Iterable<Loans> findAllLoans() {
    return loanRepo.findAll();
  }

  @GetMapping("/{uniqueUserId}")
  public @ResponseBody HashMap<String, List<Loans>> findLoanByUserId(@PathVariable int uniqueUserId) {

    HashMap<String, List<Loans>> responseBody = new HashMap<String, List<Loans>>();

    List<Loans> userLoans;

    try {

      userLoans = usrRep.findById(uniqueUserId).get().getLoans();

    } catch (Exception e) {

      throw new UnsupportedOperationException("Could not find User!");

    }

    responseBody.put("data", userLoans);

    // This is intended to find all of the accounts associated with a specific
    // uniqueUserId
    return responseBody;
  }

  /**
   * Add a new loan to the db
   * 
   * @Param loan - The loan to be added
   */

  @PostMapping("/add/{uniqueUserId}")
  public @ResponseBody Loans addLoan(@RequestBody Loans loan, @PathVariable Integer uniqueUserId){
    User foundUser = usrRep.findById(uniqueUserId).get();

      if(loan == null){
          throw new UnsupportedOperationException("Passed in 'null' for the create loan request");
      }

      loan.setUser(foundUser);
      
      loanRepo.save(loan);
      return loan; //Change this later to make a request for the gotten user
  }


  /**
     * Update loan in db
     * @Param loan - The loan to be updated, with the new values
     */

    @PutMapping("/update")
    public @ResponseBody Loans updateLoan(@RequestBody Loans loan){

      
        Optional<Loans> old = loanRepo.findById(loan.getUniqueLoanId());
        Loans oldRow = old.get();

        if(!old.isPresent()){
            throw new UnsupportedOperationException("Could not find loan with uniqueId - " + loan.getUniqueLoanId() );
        }
        if(loan.getPrincipleBalance() != null){
          oldRow.setPrincipleBalance(loan.getPrincipleBalance());
        }
        if(loan.getRemainingBalance() != null){
          oldRow.setRemainingBalance(loan.getRemainingBalance());
        }
        if(loan.getMinimumPayment() != null){
          oldRow.setMinimumPayment(loan.getMinimumPayment());
        }
        if(loan.getInterestRate() != null){
          oldRow.setInterestRate(loan.getInterestRate());
        }
        loanRepo.save(oldRow);
        return oldRow;
    }

  /**
   * Delete a loan
   * 
   * @Param the loan to be deleted, Only looks at the uid
   */

  @DeleteMapping("/delete/{uniqueLoanId}")
  public @ResponseBody String deleteLoan(@PathVariable int uniqueLoanId) {
    loanRepo.deleteById(uniqueLoanId);
    return "success";
  }

  // ----------- Exception handlers, Spring boot will call these if any of the
  // above methods return an exception specified in the "ExceptionHandler"
  // annotation

  @ExceptionHandler(SecurityException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<String> handeleSecurityException(SecurityException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handeleUnsupportedOperationException(UnsupportedOperationException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

}
