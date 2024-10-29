package org.wtm.web.user.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatedAdminDto {

    private String email;          // 사용자 이메일, ID로 사용
    private String name;           // 사용자 이름 또는 닉네임
    private String password;       // 사용자 비밀번호
    private String role;           // 사용자 권한 (예: USER, ADMIN)
    private String address;        // 주소
    private String phone;          // 전화번호
    private String profilePicture; // 프로필 사진 URL

}
