package org.wtm.web.admin.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.menu.*;
import org.wtm.web.admin.service.AdminMenuService;
import org.wtm.web.menu.model.Menu;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminMenuController {
    private final AdminMenuService adminMenuService;

    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<List<MenuListDto>> getMenus(@PathVariable Long storeId,
                                                      @RequestParam("date") String date) {
        try {
            LocalDateTime mealDate = LocalDate.parse(date).atStartOfDay();
            List<MenuListDto> menus = adminMenuService.getMenusByStoreIdAndDate(storeId, mealDate);
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/stores/{storeId}/menus", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<MenuResponseDto>> createMenus(
            @PathVariable Long storeId,
            @RequestParam("mealDate") LocalDate mealDate,
            @RequestParam("menuDtos") String menuDtosJson, // 여러 DTO
            @RequestPart(value = "imgs", required = false) List<MultipartFile> imgs
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<MenuCreateDto> menuCreateDtos = objectMapper.readValue(
                    menuDtosJson,
                    new TypeReference<List<MenuCreateDto>>() {}
            );

            MealCreateDto mealCreateDto = new MealCreateDto();
            mealCreateDto.setMealDate(mealDate);

            List<MenuResponseDto> createdMenus = adminMenuService.addMenus(storeId, mealCreateDto, menuCreateDtos, imgs);
            return new ResponseEntity<>(createdMenus, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace(); // 에러 로깅 추가
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




    @PutMapping(value ="/stores/{storeId}/menus/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestParam("mealDate") LocalDate mealDate,
            @RequestParam("menuDto") String menuDtoJson, // 단일 DTO
            @RequestParam("imgs") List<MultipartFile> imgs
    ){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MenuCreateDto menuCreateDto = objectMapper.readValue(menuDtoJson, MenuCreateDto.class);

            MealCreateDto mealCreateDto = new MealCreateDto();
            mealCreateDto.setMealDate(mealDate);

            MenuResponseDto updateMenus = adminMenuService.updateMenu(storeId, menuId, mealCreateDto, menuCreateDto, imgs);
            return new ResponseEntity<>(updateMenus, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId) {
        try {
            adminMenuService.deleteMenu(storeId, menuId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 삭제 성공 시 204 응답
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/highlighted-dates")
    public ResponseEntity<List<String>> getHighlightedDates(@RequestParam Long storeId) {
        List<String> highlightedDates = adminMenuService.getDatesWithMenus(storeId);
        return ResponseEntity.ok(highlightedDates);
    }




}
