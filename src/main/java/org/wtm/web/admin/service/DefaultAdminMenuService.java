package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.menu.*;
import org.wtm.web.admin.mapper.AdminMenuMapper;
import org.wtm.web.admin.repository.*;
import org.wtm.web.common.repository.StoreRepository;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.common.service.FileUploadService;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuCategory;
import org.wtm.web.menu.model.MenuImg;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultAdminMenuService implements AdminMenuService{

    private final AdminMenuRepository menuRepository;
    private final FileUploadService uploadService;
    private final AdminMenuMapper adminMenuMapper;
    private final AdminMenuImgRepository menuImgRepository;
    private final AdminMealRepository mealRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final AdminMenuCategoryRepository categoryRepository;

    @Value("${image.upload-menu-dir}")
    String uploadDir;

    @Override
    @Transactional
    public List<MenuListDto> getMenusByStoreIdAndDate(Long storeId, LocalDateTime date) {
        List<Menu> menus = menuRepository.findByStoreIdAndMealDate(storeId, date.toLocalDate());
        return menus.stream()
                .map(adminMenuMapper::toMenuListDto)
                .toList();
    }


    @Override
    @Transactional
    public List<MenuResponseDto> addMenus(
            Long storeId,
            MealCreateDto mealCreateDto,
            List<MenuCreateDto> menuCreateDtos,
            List<MultipartFile> imgs
    ) {
        // Store 검증
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));

        // Meal 생성 또는 조회
        if (mealCreateDto.getMealDate() == null) {
            throw new IllegalArgumentException("Meal date is required");
        }

        Meal meal = mealRepository.findByStoreAndMealDate(store, mealCreateDto.getMealDate())
                .orElseGet(() -> Meal.builder()
                        .store(store)
                        .mealDate(mealCreateDto.getMealDate())
                        .build());
        if (meal.getId() == null) {
            mealRepository.save(meal);
        }

        // 이미지 업로드 처리
        List<String> savedImgsUrl = uploadService.uploadFiles(imgs, uploadDir);

        // 각 메뉴 생성 및 저장
        List<MenuResponseDto> responseDtos = new ArrayList<>();
        for (int i = 0; i < menuCreateDtos.size(); i++) {
            MenuCreateDto menuCreateDto = menuCreateDtos.get(i);

            // 카테고리 검증
            MenuCategory category = categoryRepository.findById(menuCreateDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found for ID: " + menuCreateDto.getCategoryId()));

            // 사용자 검증
            User user = userRepository.findById(menuCreateDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + menuCreateDto.getUserId()));

            // 이미지 매핑 (리스트 내 순서를 기준으로 매핑)
            String imgUrl = savedImgsUrl.size() > i ? savedImgsUrl.get(i) : null;

            // 메뉴 생성
            Menu menu = Menu.builder()
                    .name(menuCreateDto.getName())
                    .meal(meal)
                    .store(store)
                    .user(user)
                    .category(category)
                    .build();

            menuRepository.save(menu);

            // 메뉴 이미지 생성 및 저장
            if (imgUrl != null) {
                MenuImg menuImg = MenuImg.builder()
                        .meal(meal)
                        .img(imgUrl)
                        .build();
                menuImgRepository.save(menuImg);
            }

            // 응답 생성
            responseDtos.add(MenuResponseDto.builder()
                    .message("Menu added successfully: " + menuCreateDto.getName())
                    .build());
        }

        return responseDtos;
    }




    @Override
    @Transactional
    public MenuResponseDto updateMenu(Long storeId, Long menuId,
                                      MealCreateDto mealCreateDto,
                                      MenuCreateDto menuCreateDto, // 단일 객체로 변경
                                      List<MultipartFile> imgs) throws IOException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));
        Meal meal = mealRepository.findByStoreAndMealDate(store, mealCreateDto.getMealDate())
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        // 메뉴 업데이트 로직: 입력된 DTO의 메뉴 이름과 카테고리를 기존 데이터와 비교하여 변경 사항만 업데이트
            if (!menu.getName().equals(menuCreateDto.getName())) {
                menu = Menu.builder()
                        .id(menu.getId())  // 기존 ID 유지
                        .name(menuCreateDto.getName())
                        .meal(meal)
                        .store(store)
                        .user(menu.getUser())  // 기존 User 유지
                        .category(menu.getCategory())  // 기존 카테고리 유지 (변경 사항 확인 후 업데이트)
                        .build();
            }

            // 카테고리 변경 확인
            if (menu.getCategory() == null || !menu.getCategory().getId().equals(menuCreateDto.getCategoryId())) {
                MenuCategory category = categoryRepository.findById(menuCreateDto.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Category not found"));
                menu = Menu.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .meal(meal)
                        .store(store)
                        .user(menu.getUser())
                        .category(category)
                        .build();
            }


        // 이미지 업데이트 로직
        if (!imgs.isEmpty()) {
            // 기존 이미지 삭제 후 새로운 이미지 저장
            menuImgRepository.deleteAllByMeal(meal);

            List<String> savedImgsUrl = uploadService.uploadFiles(imgs, uploadDir);
            List<MenuImg> menuImgs = new ArrayList<>();
            for (String imgUrl : savedImgsUrl) {
                MenuImg menuImg = MenuImg.builder()
                        .meal(meal)
                        .img(imgUrl)
                        .build();
                menuImgs.add(menuImg);
            }
            menuImgRepository.saveAll(menuImgs);
        }

        // 변경된 Menu 저장
        menuRepository.save(menu);

        return MenuResponseDto.builder().message("Menu updated successfully").build();
    }

    @Override
    @Transactional
    public void deleteMenu(Long storeId, Long menuId) {
        // Store 검증
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        // Menu 검증
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        // Store와 Menu의 관계 확인
        if (!menu.getStore().equals(store)) {
            throw new IllegalArgumentException("Menu does not belong to the specified store");
        }

        // Menu와 연결된 이미지 삭제
        List<MenuImg> menuImgs = menuImgRepository.findAllByMeal(menu.getMeal());
        menuImgRepository.deleteAll(menuImgs);

        // Menu 삭제
        menuRepository.delete(menu);

        // Meal과 연결된 다른 Menu가 있는지 확인
        Meal meal = menu.getMeal();
        List<Menu> remainingMenus = menuRepository.findAllByMeal(meal);
        if (remainingMenus.isEmpty()) {
            // 다른 Menu가 없으면 Meal 삭제
            mealRepository.delete(meal);
        }
    }

    @Override
    public List<String> getDatesWithMenus(Long storeId) {
        // 메뉴가 있는 일자를 데이터베이스에서 조회 (예: "2024-11-10", "2024-11-15")
        return menuRepository.findDistinctMenuDatesByStoreId(storeId);    }


}
