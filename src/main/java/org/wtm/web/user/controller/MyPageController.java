package org.wtm.web.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.user.dto.UserUpdateDto;
import org.wtm.web.user.service.MyPageService;
import org.wtm.web.user.dto.UserResponseDto;

@RestController
@RequestMapping("api/v1/user/my")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 마이페이지 조회
    @GetMapping
    public ResponseEntity<UserResponseDto> getMyPage(@RequestParam("userId") Long id) {
        UserResponseDto userResponseDto = myPageService.getMyPage(id);

        if (userResponseDto != null) {
            return ResponseEntity.ok(userResponseDto); // 200 OK와 함께 데이터 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
        }
    }

    // 사용자 정보 불러오기
    @GetMapping("/settings")
    public ResponseEntity<UserResponseDto> getMySettings(@RequestParam("userId") Long id) {
        UserResponseDto userSettings = myPageService.getMySettings(id);

        if (userSettings != null) {
            return ResponseEntity.ok(userSettings); // 200 OK와 함께 데이터 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
        }
    }


}
