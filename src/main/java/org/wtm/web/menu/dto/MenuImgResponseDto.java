package org.wtm.web.menu.dto;


import lombok.Data;

@Data
public class MenuImgResponseDto {
    private Long id;
    private String img;

    public MenuImgResponseDto(Long id, String img) {
        this.id = id;
        this.img = img;
    }
}
