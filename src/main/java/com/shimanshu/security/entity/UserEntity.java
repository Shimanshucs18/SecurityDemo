package com.shimanshu.security.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="User")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String firstName;
//  private String middleName;
    private String lastName;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    private Boolean isActive;
//    private Boolean isLocked;
//    private Boolean isDeleted;
//    private Boolean isExpired;
//    private Boolean invalidAttemptCount;
    private Date passwordUpdateDate;
    private String token;

    public void setUsername(String Shimanshu_TTN) {         
    }

    public void setFirstname(String Shimanshu) {
        
    }

    public void setMiddle(String s) {
        
    }

    public void setLastname(String Sharma) {
    }

    public void setEmail(String Shimans) {
    }

    public void setInvalidAttemptCcunt(int i) {
    }

    public void setAddress(Address address) {
    }

    public void setRoles(List<Role> roles) {
    }
}
