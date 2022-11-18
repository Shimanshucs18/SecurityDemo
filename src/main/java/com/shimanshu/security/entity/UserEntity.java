package com.shimanshu.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String firstName;
//  private String middleName;
    private String lastName;
    private String password;
    private Boolean isActive;
    private Boolean isLocked;
    private Boolean isDeleted;
    private Boolean isExpired;
    private Boolean invalidAttemptCount;
    private Date passwordUpdateDate;
    private String token;

    @OneToOne(cascade = CascadeType.ALL)
    Customer customer;



    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns=@JoinColumn(name = "user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "userEntity" ,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Address> address;

}
