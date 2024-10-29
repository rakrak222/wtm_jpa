package org.wtm.web.user.service;

import org.wtm.web.user.dto.LoginRequestDto;
import org.wtm.web.user.dto.SignUpUserDto;

public interface UserService {

    String login(LoginRequestDto loginRequestDto);

    void signup(SignUpUserDto signUpUserDto);
}
