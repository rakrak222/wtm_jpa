package org.wtm.web.user.dto.user;

import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.auth.dto.Address;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

@Getter
@Setter
public class UserUpdateDto {

    private String email;          // 사용자 이메일, ID로 사용
    private String name;           // 사용자 이름 또는 닉네임
    private String password;       // 사용자 비밀번호
    private String phone;          // 전화번호

    @Embedded
    private Address userAddress;        // 주소

    private String profilePicture; // 프로필 사진 경로 저장용

    // User 엔티티로 변환
    public User toEntity(String name, Address address, String phone, String profilePictureUrl, String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword) // 암호화된 비밀번호
                .name(name)
                .address(address)
                .phone(phone)
                .profilePicture(profilePictureUrl)
                .role(UserRole.USER) // 회원가입 기본 역할을 USER로 설정
                .build();
    }
}
