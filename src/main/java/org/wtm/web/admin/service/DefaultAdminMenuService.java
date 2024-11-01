package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


//    @Override
//    @Transactional
//    public MenuResponseDto addMenu(Long storeId, MealCreateDto mealCreateDto, List<MenuCreateDto> menuCreateDtos, List<MenuImgCreateDto> menuImgCreateDtos) throws IOException {
//        Store store = storeRepository.findById(storeId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 스토어를 찾을 수 없습니다."));
//
//        Meal meal = Meal.builder()
//                .store(store)
//                .mealDate(mealCreateDto.getMealDate())
//                .build();
//
//        List<Menu> menus = menuCreateDtos.stream().map(menuCreateDto -> {
//            MenuCategory category = menuCategoryRepository.findById(menuCreateDto.getCategoryId())
//                    .orElseThrow(()-> new IllegalArgumentException("일치하는 카테고리 ID가 없습니다."));
//
//            User user = userRepository.findById(menuCreateDto.getUserId())
//                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 ID가 없습니다."));
//
//            return Menu.builder()
//                    .store(store)
//                    .name(menuCreateDto.getName())
//                    .user(user)
//                    .category(category)
//                    .meal(meal)
//                    .build();
//        }).toList();
//
//        menuRepository.saveAll(menus);
//
//        //===================== 메뉴 이미지 저장 로직 ========================
//
//        Path directoryPath = Paths.get(uploadDir);
//        if (!Files.exists(directoryPath)) {
//            Files.createDirectories(directoryPath);
//        }
//
//        for (MenuImgCreateDto dto : menuImgCreateDtos) {
//            MultipartFile img = dto.getImg();
//            if (!img.isEmpty()) {
//                String originalFileName = img.getOriginalFilename();
//                String filePath = uploadDir + File.separator + originalFileName;
//
//                img.transferTo(new File(filePath));
//                System.out.println("파일 저장 위치: " + filePath);
//
//                MenuImg menuImg = MenuImg.builder()
//                        .img(filePath)
//                        .meal(meal)
//                        .build();
//
//                menuImgRepository.save(menuImg);
//            }
//        }
//
//
//    }

}
