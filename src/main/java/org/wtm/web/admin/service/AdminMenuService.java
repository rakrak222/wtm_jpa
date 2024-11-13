package org.wtm.web.admin.service;

import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.menu.*;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuImg;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminMenuService {

    /**
     * 메뉴 조회
     */
    public List<MenuListDto> getMenusByStoreIdAndDate(Long storeId, LocalDateTime date);


    /**
     * 메뉴 이미지 조회
     */
    public List<MenuImgDto> getMenusImgByStoreIdAndDate(Long storeId, LocalDateTime date);

    /**
     * 메뉴 등록
     */
    public List<MenuCreateDto> addMenus(
            Long storeId,
            MealCreateDto mealCreateDto,
            List<MenuCreateDto> menuCreateDtos
    );

    /**
     * 메뉴 이미지 등록
     */
    public MenuImgResponseDto addMenuPictures (
            Long storeId,
            MealCreateDto mealCreateDto,
            List<MultipartFile> imgs
    );

    /**
     * 메뉴 수정
     */
    public MenuResponseDto updateMenu(Long storeId, Long menuId,
                                      MealCreateDto mealCreateDto,
                                      MenuCreateDto menuCreateDto);

        /**
         * 메뉴 삭제
         */
    public void deleteMenu(Long storeId, Long menuId);

    /**
     * 메뉴 이미지 삭제
     */
    public MenuResponseDto deleteMenuImg (Long storeId, Long menuImgId);
}
