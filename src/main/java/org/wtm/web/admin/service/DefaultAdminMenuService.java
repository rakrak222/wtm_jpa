package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.menu.*;
import org.wtm.web.admin.mapper.AdminMenuMapper;
import org.wtm.web.admin.repository.*;
import org.wtm.web.common.repository.MenuImgRepository;
import org.wtm.web.common.service.FileUploadService;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuCategory;
import org.wtm.web.menu.model.MenuImg;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultAdminMenuService implements AdminMenuService{

    private final AdminMenuRepository menuRepository;
    private final FileUploadService uploadService;
    private final AdminMenuMapper adminMenuMapper;
    private final AdminMenuImgRepository adminMenuImgRepository;
    private final AdminMealRepository mealRepository;
    private final AdminStoreRepository storeRepository;
    private final AdminUserRepository userRepository;
    private final AdminMenuCategoryRepository categoryRepository;
    private final MenuImgRepository menuImgRepository;

    @Value("${image.upload-menu-dir}")
    String uploadDir;

    /**
     * 메뉴 조회
     */
    @Override
    @Transactional
    public List<MenuListDto> getMenusByStoreIdAndDate(Long storeId, LocalDateTime date) {
        LocalDate mealDate = date.toLocalDate();
        List<Menu> menus = menuRepository.findByStoreIdAndMealDate(storeId, mealDate);
        return menus.stream()
                .map(adminMenuMapper::toMenuListDto)
                .toList();
    }

    /**
     * 메뉴 이미지 조회
     */
    @Override
    @Transactional
    public List<MenuImgDto> getMenusImgByStoreIdAndDate(Long storeId, LocalDateTime date) {
        LocalDate mealDate = date.toLocalDate();
        List<MenuImg> imgs = adminMenuImgRepository.findByMealStoreIdAndMealMealDate(storeId, mealDate);

        return imgs.stream()
                .map(adminMenuMapper::toMenuImgDto)
                .toList();
    }

    /**
     * 메뉴 등록
     */
    @Override
    @Transactional
    public List<MenuCreateDto> addMenus(
            Long storeId,
            MealCreateDto mealCreateDto,
            List<MenuCreateDto> menuCreateDtos
    ) {
        // Store 가져오기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));

        // Meal 가져오기
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

        List<MenuCreateDto> menuCreateList = new ArrayList<>();

        // 각 메뉴 생성 및 저장
        for (MenuCreateDto menuCreateDto : menuCreateDtos) {
            // 카테고리 검증
            MenuCategory category = categoryRepository.findById(menuCreateDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found for ID: " + menuCreateDto.getCategoryId()));

            // 사용자 검증
            User user = userRepository.findById(menuCreateDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + menuCreateDto.getUserId()));

            // 메뉴 생성
            Menu menu = Menu.builder()
                    .name(menuCreateDto.getName())
                    .meal(meal)
                    .store(store)
                    .user(user)
                    .category(category)
                    .build();

            menuRepository.save(menu);

            menuCreateList.add(menuCreateDto);
        }

        return menuCreateList;
    }

    /**
     * 메뉴 이미지 등록
     */
    @Override
    @Transactional
    public MenuImgResponseDto addMenuPictures(
            Long storeId,
            MealCreateDto mealCreateDto,
            List<MultipartFile> imgs
    ) {
        // Store 가져오기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));

        // Meal 가져오기
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

        List<String> imageUrls = new ArrayList<>();

        for (String imgUrl : savedImgsUrl) {
            if(imgUrl != null) {
                MenuImg menuImg = MenuImg.builder()
                        .img(imgUrl)
                        .meal(meal)
                        .build();
                adminMenuImgRepository.save(menuImg);
                imageUrls.add(imgUrl);
            }
        }

        MenuImgResponseDto menuImgResponseDto = MenuImgResponseDto.builder()
                .imgs(imageUrls)
                .build();

        return menuImgResponseDto;
    }

    /**
     * 메뉴 수정
     */
    @Override
    @Transactional
    public MenuResponseDto updateMenu(Long storeId, Long menuId,
                                      MealCreateDto mealCreateDto,
                                      MenuCreateDto menuCreateDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));
        Meal meal = mealRepository.findByStoreAndMealDate(store, mealCreateDto.getMealDate())
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        // 이름이 변경되었을 경우 업데이트
        if (!menu.getName().equals(menuCreateDto.getName())) {
            menu.updateName(menuCreateDto.getName());
        }

        // 카테고리가 변경되었을 경우 업데이트
        if (menu.getCategory() == null || !menu.getCategory().getId().equals(menuCreateDto.getCategoryId())) {
            MenuCategory category = categoryRepository.findById(menuCreateDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            menu.updateCategory(category);
        }

        // 변경 사항은 @Transactional로 인해 자동으로 저장됨

        return MenuResponseDto.builder().message("Menu updated successfully").build();
    }

    /**
     * 메뉴 이미지 삭제
     */
    @Override
    @Transactional
    public MenuResponseDto deleteMenuImg(Long storeId, Long menuImgId){
        MenuImg deleteImg = adminMenuImgRepository.findById(menuImgId)
                .orElseThrow(() -> new IllegalArgumentException("Menu image not found"));
        adminMenuImgRepository.delete(deleteImg);

        return MenuResponseDto.builder().message("Menu image deleted successfully").build();
    }

    /**
     * 메뉴 삭제
     */
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

        // Menu 삭제
        menuRepository.delete(menu);

        // Meal과 연결된 다른 Menu가 있는지 확인
        Meal meal = menu.getMeal();
        List<Menu> remainingMenus = menuRepository.findAllByMeal(meal);
        // Meal과 연결된 MenuImg가 있는지 확인
        List<MenuImg> remainingMenuImgs = menuImgRepository.findByMealId(meal.getId());

        if (remainingMenus.isEmpty()&& remainingMenuImgs.isEmpty()) {
            // 다른 Menu가 없으면 Meal 삭제
            mealRepository.delete(meal);
        }
    }
}
