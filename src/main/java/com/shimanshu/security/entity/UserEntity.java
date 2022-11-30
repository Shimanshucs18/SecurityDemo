package com.shimanshu.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private Boolean isActive = Boolean.FALSE;
    private Boolean isLocked = Boolean.FALSE;
    private Boolean isDeleted = Boolean.FALSE;
    private Boolean isExpired = Boolean.FALSE;
    private int invalidAttemptCount = 0;
    private LocalDateTime PASSWORD_UPDATE_DATE;

    @ManyToOne
    @JoinColumn(name = "access_token_id")
    private  AccessToken accessToken;

//    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL)
//    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Role roles;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;


    public void setCustomer(Customer customer) {

    }
}