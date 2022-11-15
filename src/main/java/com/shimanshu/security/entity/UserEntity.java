package com.shimanshu.security.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Table(name="User")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String firstName;
//    private String middleName;
    private String lastName;
    private String password;
//    private Boolean isActive;
//    private Boolean isLocked;
//    private Boolean isDeleted;
//    private Boolean isExpired;
//    private Boolean invalidAttemptCount;
    private Date passwordUpdateDate;
    private String token;
}
