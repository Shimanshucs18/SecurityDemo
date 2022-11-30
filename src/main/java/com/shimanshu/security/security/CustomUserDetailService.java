package com.shimanshu.security.security;

import com.shimanshu.security.entity.Customer;
import com.shimanshu.security.entity.Role;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.CustomerRepository;
import com.shimanshu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Customer Not Found"));

        return new User(user.getEmail(),user.getPassword(),mapRolesToAuthorities(List.of(user.getRoles())));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    public CustomUserDetailService() {
    }


}
