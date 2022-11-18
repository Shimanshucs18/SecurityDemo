package com.shimanshu.security.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gst;

    private int companyContact;

    private String companyName;




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
