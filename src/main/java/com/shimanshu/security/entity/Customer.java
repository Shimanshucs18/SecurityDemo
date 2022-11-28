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
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends UserEntity  {

    @Id
    @SequenceGenerator(name = "customer_sequence",sequenceName = "customer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String contact;

    public Customer(){

    }

    public Customer(UserEntity userEntity,String contact){
        this.userEntity = userEntity;
        this.contact = contact;
    }

}
