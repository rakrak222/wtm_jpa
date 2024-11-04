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

}
