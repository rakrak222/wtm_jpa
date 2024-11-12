package org.wtm.web.auth.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wtm.web.auth.dto.UserSignUpDto;
import org.wtm.web.auth.service.UserSignUpService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserSignUpController {

    private final UserSignUpService userSignUpService;

    @PostMapping("/signUp")

    public String signUp(@RequestBody UserSignUpDto userSignUpDto) {

        userSignUpService.signUp(userSignUpDto);

        log.info(userSignUpDto.getEmail());
        log.info(userSignUpDto.getPassword());
        log.info(userSignUpDto.getRole());

        return "ok join";
    }

}
