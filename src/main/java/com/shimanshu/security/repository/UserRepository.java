package com.shimanshu.security.repository;

import com.shimanshu.security.entity.Seller;
import com.shimanshu.security.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
//    default void save(UserEntity user) {
//    }

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
