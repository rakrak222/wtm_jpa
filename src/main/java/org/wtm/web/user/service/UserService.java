package org.wtm.web.user.service;


import org.wtm.web.auth.dto.UserSignUpDto;
import org.wtm.web.user.exceptions.DuplicateEmailException;

public interface UserService {

    void signUp(UserSignUpDto userSignUpDto) throws DuplicateEmailException;
}
