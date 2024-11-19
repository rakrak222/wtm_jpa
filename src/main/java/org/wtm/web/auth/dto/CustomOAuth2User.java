package org.wtm.web.auth.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

  private final UserDto userDto;
  private final Map<String, Object> attributes; // 추가: 원시 속성 정보

  public CustomOAuth2User(UserDto userDto, Map<String, Object> attributes) {
    this.userDto = userDto;
    this.attributes = attributes; // 원시 속성 정보를 저장
  }

  @Override
  public Map<String, Object> getAttributes() {
    // 원시 속성과 UserDto 정보를 합쳐서 반환
    Map<String, Object> combinedAttributes = new HashMap<>(attributes);
    combinedAttributes.put("username", userDto.getUsername());
    combinedAttributes.put("name", userDto.getName());
    combinedAttributes.put("role", userDto.getRole());
    return combinedAttributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<>();
    collection.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {

        return userDto.getRole();
      }
    });
    return collection;
  }

  @Override
  public String getName() {

    return userDto.getName();
  }

  public String getUsername() {

    return userDto.getUsername();
  }
}