package org.wtm.web.auth.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wtm.web.auth.dto.AdminSignUpDto;
import org.wtm.web.auth.dto.CheckEmailResponseDto;
import org.wtm.web.auth.dto.UserSignUpDto;
import org.wtm.web.auth.exceptions.DuplicateEmailException;
import org.wtm.web.auth.exceptions.InvalidDataException;
import org.wtm.web.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Log4j2
public class AuthController {

    private final AuthService authService;

    @PostMapping("/admin/signUp")
    public ResponseEntity<String> adminSignUp(@RequestBody AdminSignUpDto adminSignUpDto) {
        try {
            authService.adminSignUp(adminSignUpDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided");
        }
    }

    @PostMapping("/user/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpDto userSignUpDto) {
        try {
            authService.signUp(userSignUpDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided");
        }
    }

    @PostMapping("/check-email")
    public ResponseEntity<CheckEmailResponseDto> checkEmail(@RequestBody UserSignUpDto userSignUpDto) {

        String email = userSignUpDto.getEmail();
        if(email != null) {
            boolean isDuplicate = authService.checkEmailDuplication(email);

            CheckEmailResponseDto response = new CheckEmailResponseDto(isDuplicate);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(new CheckEmailResponseDto(false));
    }

}
