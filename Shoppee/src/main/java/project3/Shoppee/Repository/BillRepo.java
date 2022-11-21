package project3.Shoppee.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project3.Shoppee.Entity.Bill;

public interface BillRepo extends JpaRepository<Bill, Integer>{
  
  @Query("SELECT b FROM Bill b "+"WHERE b.buyDate >= :start")
  Page<Bill> searchByStartDate(@Param("start") Date start ,Pageable pageable);
  
  @Query("SELECT b FROM Bill b "+"WHERE b.buyDate <= :end")
  Page<Bill> searchByEndDate(@Param("end") Date end ,Pageable pageable);
  
  @Query("SELECT b FROM Bill b WHERE b.buyDate >= :start and b.buyDate <= :end")
  Page<Bill> searchByDate(@Param("start") Date start ,@Param("end") Date end,Pageable pageable);
  
  @Query("SELECT b FROM Bill b JOIN b.user u WHERE u.id = :x")
  Page<Bill> searchByUserId(@Param("x") Integer userId ,Pageable pageable);
  
  @Query("SELECT b FROM Bill b JOIN b.user u WHERE u.name LIKE :x")
  Page<Bill> searchByUserName(@Param("x") String userName ,Pageable pageable);   
  
//  @Query("SELECT b FROM Bill b WHERE b.buyDate >= :s")
//  Page<Bill> searchByDateCreat(@Param("start") Date s );
  
  @Query("SELECT b FROM Bill b JOIN b.user u WHERE u.email = :email ")
	public List<Bill> findByUser(@Param("email") String email);

  

  
 

	

}
