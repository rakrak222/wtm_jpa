package org.wtm.web.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpDto {

  private String email;
  private String password;
  private String name;
  private String role;
  private String address;
  private String phone;
  private String profilePicture;

}
