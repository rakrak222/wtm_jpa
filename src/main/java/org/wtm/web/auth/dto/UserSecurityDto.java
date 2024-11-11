package org.wtm.web.auth.dto;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;


@ToString
@Getter
@Setter
public class UserSecurityDto extends User implements OAuth2User {

  private String email;
  private String password;
  private String name;
  private Boolean social;

  private Map<String, Object> props;

  public UserSecurityDto(String username, String password, String name, Boolean social, Collection<? extends GrantedAuthority> authorities ) {

    super(username, password, authorities);

    this.email = username;
    this.password = password;
    this.name = name;
    this.social = social;
  }

  public boolean isSocial() {
    return this.social != null && this.social;
  }

  @Override
  public String getName(){
    return this.email;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return this.getProps();
  }
}
