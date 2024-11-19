package org.wtm.web.auth.dto;

import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;
import org.wtm.web.user.constants.UserRole;

@Setter
@Getter
public class AdminSignUpDto {

    private String email;
    private String password;
    private String name;
    private String phone;
    private String role;

    // Embedded Address
    @Embedded
    private Address address;

}
