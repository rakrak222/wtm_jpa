package org.wtm.web.user.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.wtm.web.user.dto.LoginRequestDto;
import org.wtm.web.user.dto.SignUpUserDto;

public interface UserService {

    String login(LoginRequestDto loginRequestDto);

    void signUp(SignUpUserDto signUpUserDto);
}
