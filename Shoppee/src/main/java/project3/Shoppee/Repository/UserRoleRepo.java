package project3.Shoppee.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project3.Shoppee.Entity.UserRole;



public interface UserRoleRepo extends JpaRepository<UserRole, Integer>{
	@Query("SELECT ur FROM UserRole ur WHERE ur.role LIKE :x")
	  Page<UserRole> searchByRole(@Param("x") String s ,Pageable pageable);
//	@Query("SELECT ur FROM UserRole ur JOIN ur.user u WHERE u.id =:x")
//	  Page<UserRole> searchByUserId(@Param("x") int userId ,Pageable pageable);
	

}
