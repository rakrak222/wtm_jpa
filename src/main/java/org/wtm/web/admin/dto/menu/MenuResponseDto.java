package org.wtm.web.admin.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.wtm.web.menu.model.Menu;
import org.wtm.web.menu.model.MenuImg;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MenuResponseDto {
    private String message;
}

