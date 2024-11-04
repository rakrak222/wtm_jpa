package org.wtm.web.menu.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuImg;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class MenuResponseDto {
    private MealDto meal;
    private List<MenuDto> menus;

//    public MenuResponseDto(Meal meal, List<Menu> menus, List<MenuImg> mealImages) {
//        this.meal = new MealDto(meal, mealImages);
//        this.menus = menus.stream().map(MenuDto::new).collect(Collectors.toList());
//    }
}
