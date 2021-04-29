package coms.vb1.CyBank.Repos;

import coms.vb1.CyBank.Tables.*;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepo extends JpaRepository<Accounts, Integer>{
    List<Accounts> findByAccountType(Character accountType);
    
    @Query(value = "SELECT SUM(t.amount) as balance from transaction t where unique_account_id = ?", nativeQuery = true)
    Double getBalanceByAccountId(Integer uniqueAcountId);

}