package org.wtm.web.admin.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.wtm.web.menu.model.MenuImg;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class MenuListDto {
    private Long menuId;
    private Long mealId;
    private Long userId;
    private Long categoryId;
    private String name;
    private LocalDateTime menuRegDate;
    private LocalDateTime menuUpdateDate;
}
