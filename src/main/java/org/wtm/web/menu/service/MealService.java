package org.wtm.web.menu.service;

import org.wtm.web.menu.dto.MenuImgResponseDto;

import java.util.List;

public interface MealService {

    List<MenuImgResponseDto> getTodayMenuImages(Long storeId);
}
