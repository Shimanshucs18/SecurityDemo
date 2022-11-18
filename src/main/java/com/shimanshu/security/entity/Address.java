package com.shimanshu.security.entity;

import javax.persistence.*;

//@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String city;

    private String state;

    private String country;

    private String addressLine;

    private int zipCode;

    public void setCity(String mathura) {
    }

    public void setCountry(String india) {
    }

    public void setLabel(String homeTown) {
    }

    public void setState(String up) {
    }

    public void setZipcode(String s) {
    }

    public void add(Address address) {
    }

    public void setUser(UserEntity user) {
    }


//    LABEL (Ex. office/home    )
//
//    USER_ID
}
