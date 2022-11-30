package com.shimanshu.security.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@Table(name="customer")
@NoArgsConstructor
public class Customer {


//    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name="CustSeqGen",sequenceName = "CustSeq",initialValue = 1,allocationSize = 1)
    private Long id;


    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String contact;

//    public Customer() {
//
//    }
//
//    public Customer(UserEntity userEntity, String contact) {
//        this.userEntity = userEntity;
//        this.contact = contact;
//    }

}
