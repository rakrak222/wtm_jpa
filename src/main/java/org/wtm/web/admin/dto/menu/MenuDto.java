package org.wtm.web.admin.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private Long menuId;
    private String menuName;
    private Long mealId;
    private Long storeId;
    private Long userId;
    private Long categoryId;
}
