package org.wtm.web.menu.dto;


import lombok.Data;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.MenuImg;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MealDto {
    private Long mealId;
    private LocalDate mealDate;
    private List<String> images;

    public MealDto(Meal meal, List<MenuImg> images) {
        this.mealId = meal.getId();
        this.mealDate = meal.getMealDate();
        this.images = images.stream().map(MenuImg::getImg).collect(Collectors.toList());
    }
}
