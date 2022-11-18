package com.shimanshu.security.repository;


import com.shimanshu.security.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  SellerRepository extends JpaRepository<Seller,Long> {


}
