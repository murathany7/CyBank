package coms.vb1.CyBank.Tables;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import coms.vb1.CyBank.Repos.*;

@Entity
public class User{

	/* ------------------------------- 				Constructors    		---------------------------------------------*/

	/**
	 * Basic User constructor that also sets the date to the current
	 * System Date
	 */

	public User(){

		createdOn = new Date();
	}

	/**
	 * 
	 * @param uniqueUserId
	 * @param name
	 * @param email
	 * @param password
	 * @param accountType
	 * @param ssn
	 * @param age
	 */
	public User(Integer uniqueUserId, String name, String email, String password, Character accountType,
				String ssn, Integer age ){
					this.uniqueUserId = uniqueUserId;
					this.name = name;
					this.email = email;
					this.password = password;
					this.accountType = accountType;
					this.ssn = ssn;
					this.age = age;
					this.createdOn = new Date();
				}


	/* ------------------------------- 				Instance Variables    		---------------------------------------------*/

	/**
	 * Unique User Id, this is used for hibernate to identify a specific row
	 * This is also used to join with the accounts table and loans table
	 */
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer uniqueUserId;

	/**
	 * Name of the current user (Purely graphical)
	 */

	private String name;

	/**
	 * User email, used to login
	 */
	
	private String email;

	/**
	 * Users login password, is not returned by jackson when
	 * serialized for security reasons
	 */

	private String password;

	/**
	 * Account type
	 * A - admin
	 * S = support
	 * C = client
	 */
	
	private Character accountType;

	/**
	 * Created on date, always set by current system time
	 */

	private Date createdOn;

	/**
	 * Fake SSN, purely graphical
	 */

	private String ssn;

	/**
	 * Age of user, purely graphical
	 */

	private Integer age;

	/**
	 * Income of user, purely graphical
	 */

	private Integer income;

	/**
	 * List of accounts belonging to user
	 */

	@JsonIgnore
	@OneToMany(mappedBy="user")
	private List<Accounts> accounts;

	/**
	 * List of loans the user has outstanding, doesn't get passed back in response
	 */
	@JsonIgnore
	@OneToMany(mappedBy="user")
	private List<Loans> loans;

	@OneToMany(mappedBy="sentUser")
	private List<Message> sentMessages;

	@OneToMany(mappedBy="recievedUser")
	private List<Message> recievedMessages;

	/* ------------------------------- 				Methods    		---------------------------------------------*/


			/* ------------------------------- 				Accounts    	-------------------------------------*/

			public void setAccounts(List<Accounts> accounts){
				this.accounts = accounts;
			}

			public List<Accounts> getAccounts(){
				return this.accounts;
			}

			public void addAccount(Accounts account){
				this.accounts.add(account);
			}

			public void deleteAccount(Accounts account){
				this.accounts.remove(account);
			}

			public Accounts findAccountById(Integer accountId){
				for(Accounts acc : this.accounts){
					if(acc.getUniqueAccountId() == accountId){
						return acc;
					}
				}
				return null;
			}

			public void updateAccount(Accounts oldAccount, Accounts newAccount){

				updateAccount(oldAccount.getUniqueAccountId(), newAccount);

			}

			public void updateAccount(Integer oldAccountId, Accounts newAccount){

				Accounts oldAccount = findAccountById(oldAccountId);

				if(oldAccount == null ){
					throw new SecurityException("Account not found!");
				}

				if(newAccount.getAccountName() != null){
					oldAccount.setAccountName(newAccount.getAccountName());
				}
				if(newAccount.getAccountNumber() != null){
					oldAccount.setAccountNumber(newAccount.getAccountNumber());
				}
				if(newAccount.getAccountType() != null){
					oldAccount.setAccountType(newAccount.getAccountType());
				}
			}


			/* ------------------------------- 				Loans    	    -------------------------------------*/

			public void setLoans (List<Loans> loans){
				this.loans = loans;
			}

			public List<Loans> getLoans(){
				return loans;
			}


	/* --------------------------------   Basic getters and setters -------------------------------------------- */

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUniqueUserId(int userId){
		this.uniqueUserId = userId;
	}

	public Integer getUniqueUserId(){
		return uniqueUserId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setAccountType(char accountType){
		this.accountType = accountType;
	}

	public Character getAccountType(){
		return accountType;
	}

	public void setCreatedOn(Date createdOn){
		this.createdOn = createdOn;
	}

	public Date getCreatedOn(){
		return createdOn;
	}

	public void setSSN(String ssn){
		this.ssn = ssn;
	}

	public String getSSN(){
		return ssn;
	}

	public void setAge(Integer age){
		this.age = age;
	}

	public Integer getAge(){
		return age;
	}

	public void setIncome(Integer income){
		this.income = income;
	}

	public Integer getIncome(){
		return income;
	}


}
