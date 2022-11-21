package project3.Shoppee.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project3.Shoppee.Entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{
	
	@Query("SELECT pro FROM Product pro WHERE pro.name LIKE :x")
	Page<Product> searchByName(@Param("x") String productName ,Pageable pageable);
	
	@Query("SELECT pro FROM Product pro JOIN pro.category cate WHERE cate.id = :x")
	Page<Product> searchByCategoryId(@Param("x") int categoryId ,Pageable pageable);

}
