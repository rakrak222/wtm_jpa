package org.wtm.web.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wtm.web.auth.dto.UserSignUpDto;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

@Service
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpDto userSignUpDto) {

        String email = userSignUpDto.getEmail();
        String password = userSignUpDto.getPassword();
        String name = userSignUpDto.getName();

        Boolean isExist = userRepository.existsByEmail(email);

        if(isExist){
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(UserRole.USER)
            .name(name)
            .social(false)
            .build();

        userRepository.save(user);

    }

}
