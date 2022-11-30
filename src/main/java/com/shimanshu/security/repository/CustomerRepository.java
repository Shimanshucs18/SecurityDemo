package com.shimanshu.security.repository;

import com.shimanshu.security.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query(value = "SELECT a.contact from customers a WHERE a.user_id = ?1", nativeQuery = true)
    String getContactOfUserId(Long id);

    @Query(value = "SELECT * from customers a WHERE a.user_id = ?1", nativeQuery = true)
    Customer getCustomerByUserId(Long id);


}
