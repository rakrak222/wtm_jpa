package org.wtm.web.admin.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.menu.MenuCreateDto;
import org.wtm.web.admin.dto.menu.MenuImgDto;
import org.wtm.web.admin.dto.menu.MenuListDto;
import org.wtm.web.admin.dto.menu.MenuResponseDto;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuCategory;
import org.wtm.web.menu.model.MenuImg;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminMenuMapper {

    public MenuListDto toMenuListDto(Menu menu){
        // MenuImg 리스트에서 이미지 URL만 추출
        List<String> menuImages = menu.getMeal().getMenuImg().stream()
                .map(MenuImg::getImg)  // MenuImg 엔티티에서 img 필드만 추출
                .collect(Collectors.toList());

        return new MenuListDto(
                menu.getId(),
                menu.getMeal().getId(),
                menu.getUser().getId(),
                menu.getCategory().getId(),
                menu.getStore().getId(),
                menu.getName(),
                menuImages,  // 추출한 이미지 URL 리스트를 설정
                menu.getRegDate(),
                menu.getModDate()
        );
    }


    // MenuCreateDto -> Menu 엔티티 변환
    public Menu toMenuEntity(MenuCreateDto dto, Store store, MenuCategory category, User user) {
        return Menu.builder()
                .name(dto.getName())
                .store(store)
                .category(category)
                .user(user)
                .build();
    }

    // 여러 MenuCreateDto -> Menu 엔티티 리스트 변환
    public List<Menu> toMenuEntities(List<MenuCreateDto> dtos, Store store, MenuCategory category, User user) {
        return dtos.stream()
                .map(dto -> toMenuEntity(dto, store, category, user)) // 단일 엔티티 변환 메서드 재사용
                .collect(Collectors.toList());
    }

    // 여러 Menu -> MenuListDto 변환
    public List<MenuListDto> toMenuListDtos(List<Menu> menus) {
        return menus.stream()
                .map(this::toMenuListDto) // 단일 변환 메서드 재사용
                .collect(Collectors.toList());
    }



}
