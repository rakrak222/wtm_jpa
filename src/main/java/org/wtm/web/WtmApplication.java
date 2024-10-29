package org.wtm.web;

import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

@SpringBootApplication
@EnableJpaAuditing
public class WtmApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtmApplication.class, args);
    }
    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // 이미 존재하지 않는 경우에만 사용자 추가
            if (userRepository.findByEmail("example@example.com").isEmpty()) {
                User user = User.builder()
                        .email("example@example.com")
                        .password("securepassword")
                        .name("John Doe")
                        .role(org.wtm.web.user.constants.UserRole.USER)
                        .address("123 Main Street")
                        .phone("010-1234-5678")
                        .profilePicture("profile.jpg")
                        .build();

                userRepository.save(user);
            }
        };
    }

}
