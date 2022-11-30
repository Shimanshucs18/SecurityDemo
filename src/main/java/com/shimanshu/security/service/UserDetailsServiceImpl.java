package com.shimanshu.security.service;

import com.shimanshu.security.entity.Role;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.PasswordResetTokenRepo;
import com.shimanshu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final int MAX_FAILED_ATTEMPTS = 3;



    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetTokenRepo passwordResetTokenRepo;


    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with %s email", email)));
        return UserDetailsImpl.build(userEntity);
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public void disableUser(String email) {
        userRepository.disableUser(email);
    }

    public void increaseFailedAttempts(Optional<UserEntity> userEntity) {
        int newFailAttempts = userEntity.get().getInvalidAttemptCount()+1;
        userRepository.updateInvalidAttemptCount(newFailAttempts, userEntity.get().getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateInvalidAttemptCount(0, email);
    }

    public void lock(Optional<UserEntity> user) {
        user.get().setIsLocked(false);
        userRepository.save(user.get());
    }
}
