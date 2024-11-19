package org.wtm.web.menu.dto;


import lombok.Data;
import org.wtm.web.menu.model.Menu;

import java.time.LocalDate;

@Data
public class MenuDto {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private LocalDate createdTime;
    private LocalDate updatedTime;

    public MenuDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.categoryId = menu.getCategory().getId();
        this.categoryName = menu.getCategory().getName();
        this.createdTime = menu.getRegDate().toLocalDate();
        this.updatedTime = menu.getModDate().toLocalDate();
    }
}
