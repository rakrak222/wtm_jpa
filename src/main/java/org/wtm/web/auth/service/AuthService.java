package org.wtm.web.auth.service;

import org.wtm.web.auth.dto.AdminSignUpDto;
import org.wtm.web.auth.dto.UserSignUpDto;

public interface AuthService {

    void signUp(UserSignUpDto userSignUpDto);

    boolean checkEmailDuplication(String email);

    void adminSignUp(AdminSignUpDto adminSignUpDto);
}
