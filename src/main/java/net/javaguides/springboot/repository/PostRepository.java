package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.User;
import net.javaguides.springboot.model.VoterPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<VoterPost, Long>{
}
