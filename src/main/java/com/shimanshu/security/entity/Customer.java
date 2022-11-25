package com.shimanshu.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="customer")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends UserEntity  {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    Long id;

    private String contact;


//    public String getFirstName() {
//        return null;
//    }
//
//    public String getLastName() {
//        return null;
//    }
//
//    public String isActive() {
//        return null;
//    }
//
//    public void setPassword(String encode) {
//
//    }
//
//    public void setRoles(List<Role> singletonList) {
//
//    }
}
