package org.wtm.web.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.wtm.web.common.entity.BaseTimeEntity;

import java.util.Collections;
import java.util.List;
import org.wtm.web.user.constants.UserRole;

@Entity
@Table(name ="USER")
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String email; // ID

    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING) // EnumType.STRING으로 설정하여 문자열로 저장
    private UserRole role; // default 값 : ROLE_USER

    @Column(length = 100)
    private String address; // TODO : 내장 타입 사용은 추후 결정

    @Column(length = 15)
    private String phone;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "social")
    private Boolean social;

    // UserRole을 GrantedAuthority로 변환하여 반환
    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRole()));
    }
    public void updateRole(UserRole role) {
        this.role = role;
    }
    public void updateEmail(String email){ this.email = email; }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isSocial() {
        return this.social != null && this.social;
    }
}
