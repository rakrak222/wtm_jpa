package org.wtm.web.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.user.model.User;
import org.wtm.web.user.constants.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserDto {

    private String email;          // 사용자 이메일, ID로 사용
    private String name;           // 사용자 이름 또는 닉네임
    private String password;       // 사용자 비밀번호
    private String role;           // 사용자 권한 (예: USER, ADMIN)
    private String address;        // 주소
    private String phone;          // 전화번호
    private MultipartFile profilePicture; // 프로필 사진 파일 (업로드된 파일)

    // User 엔티티로 변환
    public User toEntity(String encodedPassword, String profilePictureUrl) {
        return User.builder()
                .email(email)
                .password(encodedPassword) // 암호화된 비밀번호
                .name(name)
                .address(address)
                .phone(phone)
                .profilePicture(profilePictureUrl) // 저장된 파일 URL 설정
                .role(UserRole.USER) // 회원가입 기본 역할을 USER로 설정
                .build();
    }
}
