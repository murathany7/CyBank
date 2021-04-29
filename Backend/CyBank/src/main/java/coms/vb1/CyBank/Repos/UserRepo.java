package coms.vb1.CyBank.Repos;

import coms.vb1.CyBank.Tables.*;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
	
    List<User> findByAccountType(Character accountType);
    Integer countByUniqueUserId(Integer uniqueUserId);
    
}