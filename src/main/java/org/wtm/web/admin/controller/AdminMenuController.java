package org.wtm.web.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.menu.*;
import org.wtm.web.admin.service.AdminMenuService;

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

    @GetMapping("/stores/{storeId}/menuImgs")
    public ResponseEntity<List<MenuImgDto>> getMenuImgs(@PathVariable Long storeId,
                                                        @RequestParam("date") String date) {
        try {
            LocalDateTime mealDate = LocalDate.parse(date).atStartOfDay();
            List<MenuImgDto> imgs = adminMenuService.getMenusImgByStoreIdAndDate(storeId, mealDate);
            return new ResponseEntity<>(imgs, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<?> createMenus(
            @PathVariable Long storeId,
            @RequestBody MenuRequestDto request // 여러 DTO
    ) {
        try {
            System.out.println("createMenus called with storeId: " + storeId + ", request: " + request);

            LocalDate mealDate = LocalDate.parse(request.getMealDate());
            List<MenuCreateDto> menuCreateDtos = request.getMenuDtos();

            MealCreateDto mealCreateDto = MealCreateDto.builder().mealDate(mealDate).build();
            List<MenuCreateDto> menuCreateDtoList = adminMenuService.addMenus(storeId, mealCreateDto, menuCreateDtos);
            return new ResponseEntity<>(menuCreateDtoList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/stores/{storeId}/menuImgs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuImgResponseDto> addMenuImgs(
            @PathVariable Long storeId,
            @RequestParam("mealDate") String date,
            @RequestPart(value = "imgs", required = false) List<MultipartFile> imgs
    ){
        try{
            LocalDate mealDate = LocalDate.parse(date);
            MealCreateDto mealCreateDto = MealCreateDto.builder().mealDate(mealDate).build();
            MenuImgResponseDto response = adminMenuService.addMenuPictures(storeId, mealCreateDto, imgs);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequestDto request
    ){
        try {
            System.out.println("updateMenu called with storeId: " + storeId + ", menuId: " + menuId + ", request: " + request);

            LocalDate mealDate = request.getMealDate();
            MenuCreateDto menuCreateDto = request.getMenuDto();

            MealCreateDto mealCreateDto = MealCreateDto.builder().mealDate(mealDate).build();

            MenuResponseDto updatedMenu = adminMenuService.updateMenu(storeId, menuId, mealCreateDto, menuCreateDto);
            return new ResponseEntity<>(updatedMenu, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
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

    @DeleteMapping("/stores/{storeId}/menuImgs/{menuImgId}")
    public ResponseEntity<Void> deleteMenuImg(
            @PathVariable Long storeId,
            @PathVariable Long menuImgId
    ){
        try{
            adminMenuService.deleteMenuImg(storeId, menuImgId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
