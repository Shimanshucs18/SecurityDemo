package com.shimanshu.security.repository;

import com.shimanshu.security.entity.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, Long> {
    @Query(value = "SELECT * FROM category_metadata_field_values a WHERE a.category_metadata_field_id = ?1", nativeQuery = true)
    CategoryMetadataFieldValues findByCategoryMetadataFieldId(Long metadataFieldId);
}
