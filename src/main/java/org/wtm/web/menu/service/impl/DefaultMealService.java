package org.wtm.web.menu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtm.web.common.repository.MealRepository;
import org.wtm.web.common.repository.MenuImgRepository;
import org.wtm.web.menu.dto.MenuImgResponseDto;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.MenuImg;
import org.wtm.web.menu.service.MealService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultMealService implements MealService {
    private final MealRepository mealRepository;
    private final MenuImgRepository menuImgRepository;

    public List<MenuImgResponseDto> getTodayMenuImages(Long storeId) {
        // 오늘 날짜의 Meal을 조회
        LocalDate today = LocalDate.now();
        Optional<Meal> mealOptional = mealRepository.findByStoreIdAndMealDate(storeId, today);

        if (mealOptional.isEmpty()) {
            return List.of(); // 오늘 날짜의 Meal이 없을 경우 빈 리스트 반환
        }

        // Meal에 연결된 MenuImg 목록을 조회하고 DTO로 변환
        List<MenuImg> menuImages = menuImgRepository.findByMealId(mealOptional.get().getId());
        return menuImages.stream()
                .map(img -> new MenuImgResponseDto(img.getId(), img.getImg()))
                .collect(Collectors.toList());
    }
}
