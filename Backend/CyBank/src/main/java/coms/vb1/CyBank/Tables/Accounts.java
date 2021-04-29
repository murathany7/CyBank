package coms.vb1.CyBank.Tables;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Formula;


@Entity
public class Accounts{

    /* ------------------------------- 				Constructors    		---------------------------------------------*/

	/**
	 * Basic Account constructor 
	 */

    public Accounts(){
    }

    /**
     * Constructor to be specific about a particular account mainly for testing
     * 
     * @param uniqueAccountId
     * @param user
     * @param accountNumber
     * @param accountName
     * @param accountType
     */
    public Accounts(Integer uniqueAccountId, User user, Integer accountNumber, String accountName, Character accountType){
        this.uniqueAccountId = uniqueAccountId;
        this.user = user;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountType = accountType;
    }

    /* ------------------------------- 				Instance Variables    		---------------------------------------------*/

	/**
	 * Unique account Id, this is used for hibernate to identify a specific row
	 * This is also used to join with the transaction table
	 */

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer uniqueAccountId;

    /**
     * user associated with current account, joined by the uniqueUserId
     */

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="unique_user_id", nullable=false)
    private User user;

    /**
     * accountNumber, purely graphical
     */

    private Integer accountNumber;

    /**
     * accountName, pretty name for user, used to identify account
     */

    private String accountName;

    /**
     * Single digit char accountType, examplevalues - 
     *  S - Savings
     *  M - Money Market
     *  C - Checkings
     *  R - Retirement
     */

    private Character accountType;

    /**
     * list of transactions associated with the current user account
     */

    @OneToMany(mappedBy="account")
    private List<Transaction> transactions;

    /**
     * Transient means value doesn't persist in the database
     */
    @Formula("(SELECT SUM(t.amount) from transaction t where t.unique_account_id = unique_account_id)")
    private Double balance;

    /* ------------------------------- 				Methods    		---------------------------------------------*/

            public void setTransactions(List<Transaction> transactions){
                this.transactions = transactions;
            }

            public List<Transaction> getTransactions(){
                return this.transactions;
            }

            public void addTransaction(Transaction transaction){
                if(transactions == null){
                    transactions = new ArrayList<Transaction>();
                }
                this.transactions.add(transaction);
            }

            public void deleteTransaction(Integer transactionId){
                this.transactions.remove( findTransactionById(transactionId) );
            }

            public void deleteTransaction(Transaction transaction){
                this.transactions.remove(transaction);
            }

            public Transaction findTransactionById(Integer transactionId){
                for(Transaction trans : this.transactions){
                    if(trans.getTransactionId() == transactionId){
                        return trans;
                    }
                }
                return null;
            }

            public void updateTransaction(Transaction oldTransaction, Transaction newTransaction){

                updateTransaction(oldTransaction.getTransactionId(), newTransaction);

            }

            public void updateTransaction(Integer oldTransactionId, Transaction newTransaction){

                Transaction oldTransaction = findTransactionById(oldTransactionId);

                if(oldTransaction == null ){
                    throw new SecurityException("Transaction not found!");
                }


                if(newTransaction.getAmount() != null){
                    oldTransaction.setAmount(newTransaction.getAmount());
                }
                if(newTransaction.getDescription() != null){
                    oldTransaction.setDescription(newTransaction.getDescription());
                }
                if(newTransaction.getTransactionType() != null){
                    oldTransaction.setTransactionType(newTransaction.getTransactionType());
                }
                if(newTransaction.getSubmissionDate() != null){
                    oldTransaction.setSubmissionDate(newTransaction.getSubmissionDate());
                }
            }


    /* --------------------------------   Basic getters and setters -------------------------------------------- */

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setUniqueAccountId(Integer uniqueAccountId){
        this.uniqueAccountId = uniqueAccountId;
    }

    public Integer getUniqueAccountId(){
        return uniqueAccountId;
    }

    public void setAccountNumber(Integer accountNumber){
        this.accountNumber = accountNumber;
    }

    public Integer getAccountNumber(){
        return accountNumber;
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public String getAccountName(){
        return accountName;
    }

    public void setAccountType(Character accountType){
        this.accountType = accountType;
    }

    public Character getAccountType(){
        return accountType;
    }

    @Transient
    public Double getBalance(){
        return balance;
    }

    public void setBalance(Double balance){
        this.balance = balance;
    }
}