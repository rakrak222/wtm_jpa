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

        return new MenuListDto(
                menu.getId(),
                menu.getMeal().getId(),
                menu.getUser().getId(),
                menu.getCategory().getId(),
                menu.getName(),
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


    public MenuImgDto toMenuImgDto(MenuImg menuImg){
        return new MenuImgDto(
                menuImg.getId(),
                menuImg.getImg()
        );
    }



}
