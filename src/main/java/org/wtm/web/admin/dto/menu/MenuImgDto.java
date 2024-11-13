package org.wtm.web.admin.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MenuImgDto {
    private Long id;
    private String url;
}
