package coms.vb1.CyBank.Repos;

import coms.vb1.CyBank.Tables.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepo extends JpaRepository<Transaction, Integer>{

	
}
