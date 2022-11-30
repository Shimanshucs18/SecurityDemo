package com.shimanshu.security.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String addressLine;
    private String label;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
//
//    @Override
//    public String toString() {
//        return "\tCity: "+city+"\n\tState: "+state+"\n\tCountry: "+country+"\n\tAddress Line: "+addressLine+"\n\tZip Code: "+zipcode;
//    }
}
