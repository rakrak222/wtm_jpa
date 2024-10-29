package org.wtm.web.user.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String email;          // 사용자 이메일, ID로 사용
    private String password;       // 사용자 비밀번호
}
