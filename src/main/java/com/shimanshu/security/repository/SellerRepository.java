package com.shimanshu.security.repository;


import com.shimanshu.security.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    @Query(value = "SELECT a.company_contact from sellers a WHERE a.user_id =:id", nativeQuery = true)
    String getCompanyContactOfUserId(@Param("id") Long id);

    @Query(value = "SELECT a.company_name from sellers a WHERE a.user_id =:id", nativeQuery = true)
    String getCompanyNameOfUserId(@Param("id") Long id);

    @Query(value = "SELECT a.gst_number from sellers a WHERE a.user_id =:id", nativeQuery = true)
    String getGstNumberOfUserId(@Param("id") Long id);

    @Query(value = "SELECT * from sellers a WHERE a.user_id =:id", nativeQuery = true)
    Seller getSellerByUserId(@Param("id") Long id);


}
