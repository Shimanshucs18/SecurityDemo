package com.shimanshu.security.repository;

import com.shimanshu.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
//    default void save(UserEntity user) {
//    }

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    UserEntity findUserByEmail(String email);


    @Query(value = "select u.is_Active from user u where u.id =?1", nativeQuery = true)
    boolean isUserActive(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user a SET a.isActive =:TRUE WHERE a.email =:email", nativeQuery = true)
    int enableUser(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user a SET a.isActive =:FALSE WHERE a.email =:email",nativeQuery = true)
    void disableUser(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user a SET a.invalidAttemptCount =:invalidAttemptCount WHERE a.email =:email", nativeQuery = true)
    void updateInvalidAttemptCount(@Param("invalidAttemptCount") Integer invalidAttemptCount, @Param("email") String email);


    @Query(value = "SELECT a.id, a.first_name, a.last_name, a.email, a.is_active FROM user a WHERE a.id = (SELECT user_id from user_roles where role_id = 2)", nativeQuery = true)
    List<Object[]> printPartialDataForCustomers();

    @Query(value = "SELECT a.id, a.first_name, a.last_name, a.email, a.is_active, b.company_contact, b.company_name FROM user a, sellers b WHERE a.id = (SELECT user_id from user_roles where role_id = 3) AND b.user_id = (SELECT user_id from user_roles where role_id = 3)", nativeQuery = true)
    List<Object[]> printPartialDataForSellers();

    @Query(value = "UPDATE user u SET u.invalid_attempt_count =:failAttempts WHERE u.email =:email", nativeQuery = true)
    @Modifying
    public void updateFailedAttempts(@Param("failAttempts") Integer failAttempts, @Param("email") String email);


}
