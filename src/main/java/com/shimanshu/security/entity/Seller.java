package com.shimanshu.security.entity;

import javax.persistence.*;
import java.util.List;


public class Seller {

    @Id
    private Long id;

    private Long gst;

    private int companyContact;

    private String companyName;



   @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
   @JoinTable(name="USER_ROLE",joinColumns = {@JoinColumn(name="SELLER_ID",referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name="ROLE_ID",referencedColumnName = "id")})
    List<Role> roles;

    public Seller() {
    }

    public void setEmail(String email) {
    }

    public void setPassword(String encode) {
    }

    public void setRole(List<Role> singletonList) {
    }

    public void setActive(boolean b) {

    }
}
