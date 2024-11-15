package org.wtm.web.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckEmailResponseDto {
    private boolean isDuplicate;

    public CheckEmailResponseDto(boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
    }
}
