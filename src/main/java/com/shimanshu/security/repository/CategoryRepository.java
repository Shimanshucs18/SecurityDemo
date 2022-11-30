package com.shimanshu.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<com.shimanshu.security.entity.Category, Long> {

    @Query(value = "SELECT * FROM category a WHERE a.name =:categoryName", nativeQuery = true)
    com.shimanshu.security.entity.Category findByCategoryName(@Param("categoryName") String categoryName);

    @Query(value = "SELECT * FROM category a WHERE a.parent_category_id =:id", nativeQuery = true)
    List<com.shimanshu.security.entity.Category> findChildCategories(@Param("id") Long id);

    @Query(value = "SELECT * FROM category a WHERE a.parent_category_id =:NULL", nativeQuery = true)
    List<com.shimanshu.security.entity.Category> findCategory();
}
