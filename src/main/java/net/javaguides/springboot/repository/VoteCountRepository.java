package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.VoteCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteCountRepository extends JpaRepository<VoteCount, Long>{

}
