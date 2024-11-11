package org.wtm.web.admin.service;

import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.menu.*;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuImg;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminMenuService {

    /**
     * 메뉴 조회
     */
    public List<MenuListDto> getMenusByStoreIdAndDate(Long storeId, LocalDateTime date);

    /**
     * 메뉴 등록
     */
    public List<MenuResponseDto> addMenus(
            Long storeId,
            MealCreateDto mealCreateDto,
            List<MenuCreateDto> menuCreateDtos,
            List<MultipartFile> imgs
    );
    /**
     * 메뉴 수정
     */
    public MenuResponseDto updateMenu(Long storeId, Long menuId, MealCreateDto mealCreateDto, MenuCreateDto menuCreateDto, List<MultipartFile> imgs) throws IOException;

    /**
     * 메뉴 삭제
     */
    public void deleteMenu(Long storeId, Long menuId);

    List<String> getDatesWithMenus(Long storeId);
}
