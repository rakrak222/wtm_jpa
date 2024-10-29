package org.wtm.web.menu.service;


import org.wtm.web.menu.dto.MenuRequestDto;
import org.wtm.web.menu.dto.MenuResponseDto;

public interface MenuService {
    MenuResponseDto getTodayMenusByStoreId(Long storeId);

    void addMenu(Long storeId, MenuRequestDto menuRequestDto);
}
