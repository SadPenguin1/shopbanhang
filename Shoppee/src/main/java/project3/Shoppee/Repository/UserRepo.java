package project3.Shoppee.Repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project3.Shoppee.Entity.User;


public interface UserRepo extends JpaRepository<User, Integer>{
	
	 User findByUsername(String username);
	 
	 public User findByEmail(String email);
	
	 @Query("SELECT u FROM User u WHERE u.name LIKE :x")
	  Page<User> searchByName(@Param("x") String n ,Pageable pageable);
	 
	 @Query("SELECT u FROM User u WHERE u.email LIKE :x")
	  Page<User> searchByEmail(@Param("x") String e ,Pageable pageable);
	 
	 @Query("SELECT u FROM User u WHERE u.name LIKE :x AND u.email LIKE :y")
	  Page<User> searchByNameAndEmail(@Param("x") String n ,@Param("y") String e ,Pageable pageable);
	 
//	 @Query("SELECT u FROM User u WHERE u.birthdate =:x")
//	  Page<User> searchByBirthdate(@Param("x") Date today);
	 
	 
	  
	  
	
	  
	  

}
