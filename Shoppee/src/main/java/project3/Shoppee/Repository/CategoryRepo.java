package project3.Shoppee.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project3.Shoppee.Entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{
	@Query("SELECT cate FROM Category cate WHERE cate.name LIKE :x")
	Page<Category> searchByName(@Param("x") String categoryName ,Pageable pageable);

}
