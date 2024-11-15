package org.wtm.web.auth.dto;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
public class UserSignUpDto {

  private String email;

  private String password;

  private String name;
  private String role;

  private String phone;
  private String profilePicture;

  // Embedded Address
  @Embedded
  private Address address;

}
