package coms.vb1.CyBank.Classes;

import coms.vb1.CyBank.Tables.Accounts;
import coms.vb1.CyBank.Tables.User;

import java.util.List;

/**
 * Class to represent a report of the stats of particular user.
 */
public class Report {

    /* ------------------------------- 				Constructors    		---------------------------------------------*/

    /**
     * Basic Constructor
     */
    public Report(){

    }


    /**
     * Sets the user for the report to the user in the parameter
     * @param user User to tie to the report
     */
    public Report(User user){
        this.user = user;
    }


    private User user; //Instance of a user per report

    /**
     * 
     * @return number of accounts the user has
     */
    public Integer getNumAccounts(){
        return user.getAccounts().size();
    }


    /**
     * 
     * @return number of transactions user has among all accounts
     */
    public Integer getNumTransactions(){
        List<Accounts> accList = user.getAccounts();
        Integer transactionTotal = 0;
        for(Accounts a: accList){
            transactionTotal += a.getTransactions().size();
        }
        return transactionTotal;
    }

    /**
     * 
     * @return number of loans the user has
     */
    public Integer getNumLoans(){
        return user.getLoans().size();
    }

    /**
     * 
     * @return grand total of the balances between all accounts
     */
    public Double getTotalBalance(){
        List<Accounts> accList = user.getAccounts();
        Double transactionTotal = 0.00;
        for(Accounts a: accList){
            transactionTotal += a.getBalance();
        }
        return transactionTotal;
    }

    /**
     * 
     * @return name of the account with the highest balance
     */
    public String getHighestValueAccount(){
        List<Accounts> accList = user.getAccounts();
        String name = accList.get(0).getAccountName();
        Double highestValue = accList.get(0).getBalance();
        for(Accounts a: accList){
            if(highestValue < a.getBalance()){
                highestValue = a.getBalance();
                name = a.getAccountName();
            }
        }
        return name;
    }

    /**
     * 
     * @return name of the account with lowest balance
     */
    public String getLowestValueAccount(){
        List<Accounts> accList = user.getAccounts();
        String name = accList.get(0).getAccountName();
        Double lowestValue = accList.get(0).getBalance();
        for(Accounts a: accList){
            if(lowestValue > a.getBalance()){
                lowestValue = a.getBalance();
                name = a.getAccountName();
            }
        }
        return name;
    }

    /**
     * 
     * @return average account balance
     */
    public Integer getAverageAccountBalance(){
        List<Accounts> accList = user.getAccounts();
        Double transactionTotal = 0.00;
        for(Accounts a: accList){
            transactionTotal += a.getBalance();
        }
        return (int)(transactionTotal / accList.size());
    }

    /**
     * 
     * @return average transaction amount
     */
    public Integer getAverageTransactionAmount(){
        List<Accounts> accList = user.getAccounts();
        double transactionTotal = 0.00;
        double sum = 0.00;
        for(Accounts a: accList){
            transactionTotal += a.getBalance();
            transactionTotal /= a.getTransactions().size();
            sum += transactionTotal;
        }
        return (int)(sum / accList.size());
    }
    
}
