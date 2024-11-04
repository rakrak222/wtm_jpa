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
    public List<MenuListDto> getMenusByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreId(storeId);
        return menus.stream()
                .map(adminMenuMapper::toMenuListDto)
                .toList();
    }

    @Override
    @Transactional
    public MenuResponseDto addMenu(Long storeId, MenuRequestDto menuRequestDto, List<MultipartFile> imgs) throws IOException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        User user = userRepository.findById(menuRequestDto.getMenuCreateDtos().getFirst().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(menuRequestDto.getMealCreateDto().getMealDate() == null) {
            throw new IllegalArgumentException("Meal date is required");
        }

        Meal meal = mealRepository.findByStoreAndMealDate(store, menuRequestDto.getMealCreateDto().getMealDate())
                .orElseGet(() -> Meal.builder()
                        .store(store)
                        .mealDate(menuRequestDto.getMealCreateDto().getMealDate())
                        .build());

// 새로운 Meal 인스턴스일 경우만 저장
        if (meal.getId() == null) {
            mealRepository.save(meal);
        }

        // 메뉴 이미지 업로드 처리
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

        // 메뉴 업로드 처리
        List<Menu> menus = new ArrayList<>();
        for (MenuCreateDto menuCreateDto : menuRequestDto.getMenuCreateDtos()) {
            MenuCategory category = categoryRepository.findById(menuCreateDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));

            Menu menu = Menu.builder()
                    .name(menuCreateDto.getName())
                    .meal(meal)
                    .store(store)
                    .user(user)
                    .category(category)
                    .build();
            menus.add(menu);
        }
        menuRepository.saveAll(menus);

        MenuResponseDto response = MenuResponseDto.builder().message("hi").build();
        return response;
    }

    @Override
    @Transactional
    public MenuResponseDto updateMenu(Long storeId, Long menuId, MenuRequestDto menuRequestDto, List<MultipartFile> imgs) throws IOException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));
        Meal meal = mealRepository.findByStoreAndMealDate(store, menuRequestDto.getMealCreateDto().getMealDate())
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        // 메뉴 업데이트 로직: 입력된 DTO의 메뉴 이름과 카테고리를 기존 데이터와 비교하여 변경 사항만 업데이트
        for (MenuCreateDto menuCreateDto : menuRequestDto.getMenuCreateDtos()) {
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




}
