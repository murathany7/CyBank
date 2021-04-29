package coms.vb1.CyBank.Tables;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Loans{

    /* ------------------------------- 				Constructors    		---------------------------------------------*/

	/**
	 * Basic User constructor that also sets the date to the current
	 * System Date
	 */

    public Loans(){
        createdOn = new Date();
    }

    public Loans(User user, Integer uniqueLoanId, Double principleBalance, Double remainingBalance, Double minimumPayment, Double interestRate){
        this.user = user;
        this.principleBalance = principleBalance;
        this.remainingBalance = remainingBalance;
        this.minimumPayment = minimumPayment;
        this.interestRate = interestRate;
        this.createdOn = new Date();
    }
    
    /* ------------------------------- 				Instance Variables    		---------------------------------------------*/

    /**
     * Unique loan id, used purely for hibernate to identify users
     */

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer uniqueLoanId;

    /**
     * User associated with the current loan, used to to join with User table
     */

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="unique_user_id", nullable=false)
    private User user;

    /**
     * The initial cost of the loan
     */
    private Double principleBalance;

    /**
     * The remaining balance to be paid off 
     */
    private Double remainingBalance;

    /**
     * Minimum payment on the loan
     */
    private Double minimumPayment;

    /**
     * The date of the loan's was creation
     */
    private Date createdOn;

    /**
     * Interest rate on loan
     */
    private Double interestRate;

    
    /* ------------------------------- 				Methods    		---------------------------------------------*/


    /* --------------------------------   Basic getters and setters -------------------------------------------- */

    public void setCreatedOn(Date createdOn){
        this.createdOn = createdOn;
    }

    public Date getCreatedOn(){
        return createdOn;
    }

    public void setUniqueLoanId(Integer uniqueLoanId){
        this.uniqueLoanId = uniqueLoanId;
    }

    public Integer getUniqueLoanId(){
        return uniqueLoanId;
    }

    public void setInterestRate(double interestRate){
        this.interestRate = interestRate;
    }

    public Double getInterestRate(){
        return interestRate;
    }

    public void setRemainingBalance(Double remainingBalance){
        this.remainingBalance = remainingBalance;
    }

    public Double getRemainingBalance(){
        return remainingBalance;
    }

    public void setPrincipleBalance(Double principleBalance){
        this.principleBalance = principleBalance;
    }

    public Double getPrincipleBalance(){
        return principleBalance;
    }

    public void setMinimumPayment(Double minimumPayment){
        this.minimumPayment = minimumPayment;
    }

    public Double getMinimumPayment(){
        return minimumPayment;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

}