package org.wtm.web.auth.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.auth.dto.Address;
import org.wtm.web.auth.dto.AdminSignUpDto;
import org.wtm.web.auth.dto.UserSignUpDto;
import org.wtm.web.auth.exceptions.DuplicateEmailException;
import org.wtm.web.auth.service.AuthService;
import org.wtm.web.common.repository.StoreRepository;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

@RequiredArgsConstructor
@Log4j2
@Service
public class DefaultAuthService implements AuthService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void adminSignUp(AdminSignUpDto adminSignUpDto) {

        String email = adminSignUpDto.getEmail();
        String password = adminSignUpDto.getPassword();
        String name = adminSignUpDto.getName();
        String phone = adminSignUpDto.getPhone();
        Address address = adminSignUpDto.getAddress();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new DuplicateEmailException();
        }

        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .name(name)
            .phone(phone)
            .social(false)
            .address(address)
            .role(UserRole.ADMIN)
            .build();

        Store store = Store.builder()
            .name(name)
            .contact(phone)
            .user(user)
            .address(address)
            .build();

        userRepository.save(user);
        storeRepository.save(store);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public Optional<Store> getStoreByUser(User user) {
        return storeRepository.findStoreByUser(user);
    }

    @Override
    @Transactional
    public void signUp(UserSignUpDto userSignUpDto) {

        String email = userSignUpDto.getEmail();
        String password = userSignUpDto.getPassword();
        String name = userSignUpDto.getName();
        String phone = userSignUpDto.getPhone();
        Address address = userSignUpDto.getAddress();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        try {
            User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .phone(phone)
                .social(false)
                .address(address)
                .role(UserRole.USER)
                .build();

            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }

    @Override
    public boolean checkEmailDuplication(String email) {

        boolean exists = userRepository.existsByEmail(email);
        if (!exists) {
            return false;
        }
        return true;
    }
}
