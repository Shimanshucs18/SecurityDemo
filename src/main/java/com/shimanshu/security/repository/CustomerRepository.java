package com.shimanshu.security.repository;

import com.shimanshu.security.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    public List<Customer> findAll();
    Optional<Customer>findByEmail(String email);
//    public Optional<Customer> findByEmail(String email);
//    public boolean existsByEmail(String email);
//

}
