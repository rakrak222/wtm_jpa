package org.wtm.web.admin.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuUpdateRequestDto {
    private MealCreateDto mealCreateDto;
    private List<MenuCreateDto> menuCreateDtos;
}
