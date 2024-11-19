package org.wtm.web.auth.contoller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wtm.web.auth.dto.AdminSignUpDto;
import org.wtm.web.auth.dto.CheckEmailResponseDto;
import org.wtm.web.auth.dto.CustomOAuth2User;
import org.wtm.web.auth.dto.UserSignUpDto;
import org.wtm.web.auth.exceptions.DuplicateEmailException;
import org.wtm.web.auth.exceptions.InvalidDataException;
import org.wtm.web.auth.service.AuthService;
import org.wtm.web.store.model.Store;
import org.wtm.web.store.service.StoreService;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = extractUsername(authentication);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<User> userOptional = authService.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();
        Map<String, Object> body = new HashMap<>();
        body.put("username", user.getEmail());
        body.put("role", user.getRole().name());

        if (user.getRole() == UserRole.ADMIN) {
            Optional<Store> storeOptional = authService.getStoreByUser(user);
            body.put("storeId", storeOptional.map(Store::getId).orElse(null));
        }

        return ResponseEntity.ok(body);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 즉시 만료
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out");
    }

    private String extractUsername(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            return oauth2User.getAttribute("username");
        } else if (authentication.getPrincipal() instanceof User user) {
            return user.getEmail();
        }
        return null;
    }
}

