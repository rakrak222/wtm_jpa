package org.wtm.web.auth.service;

import java.util.Optional;
import org.wtm.web.auth.dto.AdminSignUpDto;
import org.wtm.web.auth.dto.UserSignUpDto;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

public interface AuthService {

    void signUp(UserSignUpDto userSignUpDto);

    boolean checkEmailDuplication(String email);

    void adminSignUp(AdminSignUpDto adminSignUpDto);

    Optional<User> getUserByUsername(String username);

    Optional<Store> getStoreByUser(User user);

}
