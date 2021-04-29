package coms.vb1.CyBank.Tables;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Transaction{

    /* ------------------------------- 				Constructors    		---------------------------------------------*/

	/**
	 * Basic Transaction constructor that also sets the submission Date to the current
	 * System Date
	 */

    public Transaction(){

        submissionDate = new Date();
    }

    /**
     * Constructor to be more explicit when creating a transaction. Used for testing.
     * 
     * @param account
     * @param transactionId
     * @param amount
     * @param description
     * @param transactionType
     */
    public Transaction(Accounts account, Integer transactionId, Double amount, String description, Integer transactionType){
        this.account = account;
        user = this.account.getUser();
        this.transactionId = transactionId;
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
        submissionDate = new Date();
    }

    /* ------------------------------- 				Instance Variables    		---------------------------------------------*/

    /**
     * Account associated with the current transaction, used to join with the 
     * account table
     */

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="unique_account_id", nullable=false)
    private Accounts account;

     /**
     * user associated with current transaction, joined by the uniqueUserId. This is needed for saving transactions within constraint
     */

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="unique_user_id", nullable=false)
    private User user;

    /**
     * Transaction Id is the unique id used soley for hibernate to differentaite transactions
     */

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer transactionId;

    /**
     * Value of the current transaction, can be either positive or negative
     */

    private Double amount;

    /**
     * Description of the transaction, used soley for user clarification
     */

    private String description;

    /**
     * Type of transaction, the value of this corresponds to the R.drawable enum in android
     */

    private Integer transactionType;

    /**
     * Submission date, normally this will be the value right when the Transaction constructor
     * Was called
     */

    private Date submissionDate;

    /* ------------------------------- 				Methods    		---------------------------------------------*/

    /* --------------------------------   Basic getters and setters -------------------------------------------- */


    
    public void setTransactionId(Integer transactionId){
        this.transactionId = transactionId;
    }

    public Integer getTransactionId(){
        return transactionId;
    }

    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Double getAmount(){
        return amount;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setSubmissionDate(Date submissionDate){
        this.submissionDate = submissionDate;
    }

    public Date getSubmissionDate(){
        return submissionDate;
    }

    public void setAccount(Accounts account){
        this.account = account;
    }

    public Accounts getAccount(){
        return account;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setTransactionType(Integer transactionType){
        this.transactionType = transactionType;
    }

    public Integer getTransactionType(){
        return transactionType;
    }
    
}
