package com.shimanshu.security.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "sellers")
public class Seller  {

    @SequenceGenerator(name = "seller_sequence", sequenceName = "seller_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seller_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    private String gstNumber;
    private String companyContact;
    private String companyName;

    public Seller() {

    }

    public Seller(UserEntity userEntity, String gstNumber, String companyContact, String companyName) {
        this.userEntity = userEntity;
        this.gstNumber = gstNumber;
        this.companyContact = companyContact;
        this.companyName = companyName;


    }
}
