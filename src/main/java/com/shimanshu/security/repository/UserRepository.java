package com.shimanshu.security.repository;

 import com.shimanshu.security.entity.Customer;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<Customer,Long> {
}
