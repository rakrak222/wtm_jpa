package org.wtm.web.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class MenuRequestDto {
    private String mainMenu;
    private String soupMenu;
    private List<String> etcMenus;
    private List<MultipartFile> menuImages;
}
