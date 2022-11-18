package com.shimanshu.security;

import com.shimanshu.security.entity.Address;
import com.shimanshu.security.entity.Role;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.RoleRepository;
import com.shimanshu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/*
public class Bootstrap<Roles> implements ApplicationRunner {
    @Autowired
    RoleRepository rolesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void run(ApplicationArguments args) throws Exception {
        if ( userRepository.count() < 1) {
            UserEntity user = new UserEntity();
            user.setUsername("Shimanshu_TTn");
            user.setFirstname("Shimanshu");
            user.setMiddle("");
            user.setLastname("Sharma");
            user.setEmail("Shimanshu.sharma@tothenew.com");
//          user.setMobile("9012230506");

          user.setPassword(bCryptPasswordEncoder.encode("Admin@123"));
          user.setInvalidAttemptCcunt(3);

            List<Address> addresses = new ArrayList<>();
            Address address = new Address();
            address.setCity("Mathura");
            address.setCountry("India");
            address.setLabel("HomeTown");
            address.setState("UP");
            address.setZipcode("281122");
            address.add(address);

            address.setUser(user);

            user.setAddress(address);

            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setAuthority("ROLE_ADMIN");

            roles.add((Role) role);
            user.setRoles(roles);

            userRepository.save(user);
            System.out.println("Total users Saved::" + userRepository.count());


        }
    }
}
*/
