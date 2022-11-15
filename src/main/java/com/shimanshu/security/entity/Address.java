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


//    LABEL (Ex. office/home)
//
//    USER_ID
}
