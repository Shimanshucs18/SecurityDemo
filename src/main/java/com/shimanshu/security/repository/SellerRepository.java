package com.shimanshu.security.repository;


import com.shimanshu.security.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface  SellerRepository extends JpaRepository<Seller,Long> {

    @Query(value = "SELECT a.company_contact from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    String getCompanyContactOfUserId(Long id);

    @Query(value = "SELECT a.company_name from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    String getCompanyNameOfUserId(Long id);

    @Query(value = "SELECT a.gst_number from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    String getGstNumberOfUserId(Long id);

    @Query(value = "SELECT * from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    Seller getSellerByUserId(Long id);

    static void save(Optional<Seller> seller) {
    }

    Optional<Seller>findByEmail(String email);
    Boolean existsByEmail(String email);



}
