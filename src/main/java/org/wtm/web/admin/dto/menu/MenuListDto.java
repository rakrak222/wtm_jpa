package org.wtm.web.admin.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuListDto {
    private Long menuId;
    private Long mealId;
    private Long storeId;
    private Long userId;
    private Long categoryId;
    private String name;
}
