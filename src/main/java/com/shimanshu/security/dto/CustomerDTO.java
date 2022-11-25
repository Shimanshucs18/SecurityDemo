package com.shimanshu.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private long id;

    private String email;
    private  String firstName;
    private String middleName;
    private String lastName;
    private boolean isActive=false;
}
