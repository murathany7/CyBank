package coms.vb1.CyBank.Repos;

import coms.vb1.CyBank.Tables.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Integer>{

}