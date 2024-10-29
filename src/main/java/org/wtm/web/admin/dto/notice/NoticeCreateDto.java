package org.wtm.web.admin.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeCreateDto {
    private Long storeId;
    private String title;
    private String content;
    private Long userId;
}
