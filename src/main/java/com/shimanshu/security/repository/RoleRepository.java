package com.shimanshu.security.repository;

import com.shimanshu.security.entity.Role;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Long> {

    Optional<Role> findByRole(String role);

    Optional<Role> findByAuthority(String role);
}
