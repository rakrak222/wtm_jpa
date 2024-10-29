package org.wtm.web.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.user.dto.JwtResponseDto;
import org.wtm.web.user.dto.LoginRequestDto;
import org.wtm.web.user.dto.SignUpUserDto;
import org.wtm.web.user.exceptions.InvalidCredentialsException;
import org.wtm.web.user.exceptions.UserNotFoundException;
import org.wtm.web.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.login(loginRequestDto);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpUserDto signUpUserDto) {
        userService.signup(signUpUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    // User 관련 예외에 대한 공통 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}
