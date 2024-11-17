package org.wtm.web.menu.service.impl;



import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.common.repository.*;
import org.wtm.web.common.service.UploadService;
import org.wtm.web.menu.dto.MenuRequestDto;
import org.wtm.web.menu.dto.MenuResponseDto;
import org.wtm.web.menu.mapper.MenuMapper;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuCategory;
import org.wtm.web.menu.model.MenuImg;
import org.wtm.web.menu.service.MenuService;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultMenuService implements MenuService {

    private final MealRepository mealRepository;
    private final MenuRepository menuRepository;
    private final MenuImgRepository menuImgRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final StoreRepository storeRepository;
    private final MenuMapper menuMapper;
    private final UserRepository userRepository;
    private final UploadService uploadService;

    @Value("${image.upload-menu-dir}")
    private String uploadDir;



    @Override
    public MenuResponseDto getTodayMenusByStoreId(Long storeId) {
        // Store 객체를 조회하여 Menu 생성자에 전달

        LocalDate today = LocalDate.now();


        // 오늘 날짜의 Meal 찾기
        Optional<Meal> mealOptional = mealRepository.findByMealDateAndStoreId(today, storeId);
        if (mealOptional.isEmpty()) {
            return null;
        }
        Meal meal = mealOptional.get();

        // 해당 Meal의 메뉴와 이미지 찾기
        List<Menu> menus = menuRepository.findByStoreIdAndMealId(storeId, meal.getId());
        List<MenuImg> mealImages = menuImgRepository.findByMealId(meal.getId());

        return MenuMapper.toDto(meal, menus, mealImages);
    }

    @Override
    @Transactional
    public void addMenu(Long storeId, MenuRequestDto menuRequestDto, Long userId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 Store를 찾을 수 없습니다: " + storeId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 User를 찾을 수 없습니다: " + userId));

        // 오늘 날짜를 설정
        LocalDate today = LocalDate.now();

        // 해당 가게에 오늘의 Meal이 있는지 확인하고 없으면 생성
        Meal meal = mealRepository.findByMealDateAndStoreId(today, storeId)
                .orElseGet(() -> {
                    Meal newMeal = Meal.builder()
                            .mealDate(today)
                            .store(store)
                            .build();
                    mealRepository.save(newMeal);
                    return newMeal;
                });

        // 카테고리 조회
        MenuCategory mainCategory = menuCategoryRepository.findByName("메인메뉴")
                .orElseThrow(() -> new RuntimeException("Main Menu 카테고리를 찾을 수 없습니다."));
        MenuCategory soupCategory = menuCategoryRepository.findByName("국메뉴")
                .orElseThrow(() -> new RuntimeException("Soup Menu 카테고리를 찾을 수 없습니다."));
        MenuCategory etcCategory = menuCategoryRepository.findByName("서브메뉴")
                .orElseThrow(() -> new RuntimeException("Etc Menu 카테고리를 찾을 수 없습니다."));



        // 메뉴 등록

        menuRepository.save(menuMapper.toEntity(menuRequestDto.getMainMenu(), mainCategory, meal, store, user));
        menuRepository.save(menuMapper.toEntity(menuRequestDto.getSoupMenu(), soupCategory, meal, store, user));


//        null일 경우 nullpointerException 발생 >> null 일경우 빈리스트로 초기화시키는 과정 필요 or optional 사용
        List<Menu> etcMenuEntities = menuMapper.toEntityList(menuRequestDto.getEtcMenus(), etcCategory, meal, store, user);
        menuRepository.saveAll(etcMenuEntities);

        // 이미지 저장
        if (menuRequestDto.getMenuImages() != null) {


            for (MultipartFile menuImage : menuRequestDto.getMenuImages()) {
                String savedFileUrl = uploadService.uploadFile(menuImage, uploadDir);
                MenuImg menuImg = MenuImg.builder()
                        .meal(meal)
                        .img(savedFileUrl)
                        .build();
                menuImgRepository.save(menuImg);
            }
        }


//        // 이미지 저장
//        if (menuRequestDto.getFiles() != null) {
//            for (MultipartFile file : menuRequestDto.getFiles()) {
//                try {
//                    // File 경로 설정 및 파일 저장
//                    String filePath = "/res/menuImgs/" + file.getOriginalFilename();
//                    File destinationFile = new File(filePath);
//                    file.transferTo(destinationFile);
//
//                    // MenuImg 객체 생성
//                    MenuImg menuImg = MenuImg.builder()
//                            .meal(meal)
//                            .img(filePath)
//                            .build();
//
//                    menuImgRepository.save(menuImg);
//                } catch (IOException e) {
//                    throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
//                }
//            }
//        }
    }

}
