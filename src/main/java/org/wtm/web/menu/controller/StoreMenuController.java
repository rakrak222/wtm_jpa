package org.wtm.web.menu.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.menu.dto.MenuImgResponseDto;
import org.wtm.web.menu.dto.MenuRequestDto;
import org.wtm.web.menu.dto.MenuResponseDto;
import org.wtm.web.menu.service.MealService;
import org.wtm.web.menu.service.MenuService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreMenuController {

    private final MenuService menuService;
    private final MealService mealService;

    @GetMapping("/{storeId}/menus/today-images")
    public List<MenuImgResponseDto> getTodayMenuImages(@PathVariable Long storeId) {
        return mealService.getTodayMenuImages(storeId);
    }


    @GetMapping("/{storeId}/menus/today")
    public ResponseEntity<?> getTodayMenusByStoreId(@PathVariable Long storeId) {
        MenuResponseDto response = menuService.getTodayMenusByStoreId(storeId);
        if (response == null) {
            return ResponseEntity.status(404).body("오늘의 메뉴를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{storeId}/menus")
    public ResponseEntity<?> addMenu(@PathVariable Long storeId,
                                     @ModelAttribute MenuRequestDto menuRequestDto,
                                     @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        menuRequestDto.setMenuImages(files);

        Long userId = 1L; // 테스트를 위해 userId를 1로 고정

        menuService.addMenu(storeId, menuRequestDto, userId);
        return ResponseEntity.status(201).body("메뉴가 성공적으로 등록되었습니다.");
    }



}
