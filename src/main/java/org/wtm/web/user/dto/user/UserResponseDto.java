package org.wtm.web.user.dto.user;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wtm.web.auth.dto.Address;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String email;          // 사용자 이메일, ID로 사용
    private String name;           // 사용자 이름 또는 닉네임
    private String password;       // 사용자 비밀번호
    private String role;           // 사용자 권한 (예: USER, ADMIN)

    @Embedded
    private Address address;        // 주소

    private String phone;          // 전화번호
    private String profilePicture; // 프로필 사진 URL

}
