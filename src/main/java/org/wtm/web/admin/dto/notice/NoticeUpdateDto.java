package org.wtm.web.admin.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeUpdateDto {
    private String title;
    private String content;
}
