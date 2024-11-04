package org.wtm.web.admin.service;

import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.menu.*;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuImg;

import java.io.IOException;
import java.util.List;

public interface AdminMenuService {

    /**
     * 메뉴 조회
     */
    public List<MenuListDto> getMenusByStoreId(Long storeId);

    /**
     * 메뉴 등록
     */
    public MenuResponseDto addMenu(Long storeId, MenuRequestDto menuRequestDto, List<MultipartFile> imgs) throws IOException;


}
