package org.wtm.web.menu.service;


import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.menu.dto.MenuRequestDto;
import org.wtm.web.menu.dto.MenuResponseDto;

import java.io.IOException;

public interface MenuService {
    MenuResponseDto getTodayMenusByStoreId(Long storeId);

    void addMenu(Long storeId, MenuRequestDto menuRequestDto, Long userId) throws IOException;

}
