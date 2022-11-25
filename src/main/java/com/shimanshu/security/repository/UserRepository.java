package com.shimanshu.security.repository;

import com.shimanshu.security.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
//    default void save(UserEntity user) {
//    }

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);


    @Query(value = "select u.is_Active from user u where u.id =?1", nativeQuery = true)
    boolean isUserActive(Long id);

       Optional<UserEntity> findById(Long id);

    UserEntity findUserByEmail(String email);

    void setEmail(String email);
}
