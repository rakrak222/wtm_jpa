package org.wtm.web.auth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

  private final Map<String, Object> attribute;

  public KakaoResponse(Map<String, Object> attribute) {

    this.attribute = attribute;
  }

  @Override
  public String getProvider() {

    return "kakao";
  }

  @Override
  public String getProviderId() {
      // 최상위에 있는 "id" 값을 가져옵니다.
      Object id = attribute.get("id");
      return id != null ? id.toString() : null;
  }

  @Override
  public String getEmail() {
    // "kakao_account" 맵에서 "email" 값을 가져옵니다.
    Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
    if (kakaoAccount != null) {
      Object email = kakaoAccount.get("email");
      return email != null ? email.toString() : null;
    }
    return null;
  }

  @Override
  public String getName() {
    // "properties" 맵에서 "nickname" 값을 가져옵니다.
    Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
    if (properties != null) {
      Object nickname = properties.get("nickname");
      return nickname != null ? nickname.toString() : null;
    }
    return null;
  }
}