package com.shimanshu.security.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "sellers")
@NoArgsConstructor
public class Seller{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity userEntity;

    @Column(nullable = false)
    private String gstNumber;

    @Column(nullable = false)
    private String companyContact;

    @Column(nullable = false)
    private String companyName;

    public Seller(UserEntity userEntity, String gstNumber, String companyContact, String companyName) {
        this.userEntity = userEntity;
        this.gstNumber = gstNumber;
        this.companyContact = companyContact;
        this.companyName = companyName;
    }
}
