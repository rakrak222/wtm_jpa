package org.wtm.web.admin.controller;

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
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminMenuController {
    private final AdminMenuService adminMenuService;

    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<List<MenuListDto>> getMenus(@PathVariable Long storeId) {
        try {
            List<MenuListDto> menus = adminMenuService.getMenusByStoreId(storeId);
            return new ResponseEntity<>(menus, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<MenuResponseDto> createMenu(
            @PathVariable Long storeId,
            @ModelAttribute MenuRequestDto menuRequestDto,
            @RequestParam("imgs") List<MultipartFile> imgs
    ){
        try {
            MenuResponseDto createMenus = adminMenuService.addMenu(storeId, menuRequestDto, imgs);
            return new ResponseEntity<>(createMenus, HttpStatus.CREATED);
        } catch (IOException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
