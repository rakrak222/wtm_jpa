package org.wtm.web.menu.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.menu.dto.MealDto;
import org.wtm.web.menu.dto.MenuDto;
import org.wtm.web.menu.dto.MenuResponseDto;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuCategory;
import org.wtm.web.menu.model.MenuImg;
import org.wtm.web.store.model.Store;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MenuMapper {

    public Menu toEntity(String menuName, MenuCategory category, Meal meal, Store store) {
        return Menu.builder()
                .name(menuName)
                .category(category)
                .meal(meal)
                .store(store)
                .build();
    }
    public List<Menu> toEntityList(List<String> etcMenus, MenuCategory category, Meal meal, Store store) {
        return Optional.ofNullable(etcMenus).orElse(Collections.emptyList())
                .stream()
                .map(menuName -> toEntity(menuName, category, meal, store))
                .collect(Collectors.toList());
    }

    public static MenuResponseDto toDto(Meal meal, List<Menu> menus, List<MenuImg> mealImages) {
        return MenuResponseDto.builder()
                .meal(new MealDto(meal, mealImages))
                .menus(menus.stream().map(MenuDto::new).collect(Collectors.toList()))
                .build();
    }

}
