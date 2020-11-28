package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByCode(String code);
	User findByCodeAndContact(String code, String contact);
	User findByCodeAndPassword(String code, String password);

	@Query("SELECT u FROM User u WHERE u.posts.size > 0")
	List<User> findNominatedUsers();

//	@Query("SELECT u FROM User u where u.id < 2000")
//	List<User> findFirstHundred();
}
