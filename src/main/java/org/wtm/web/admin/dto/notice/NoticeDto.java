package org.wtm.web.admin.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeDto {
    private Long noticeId;
    private Long storeId;
    private Long userId;
    private String title;
    private String content;

}
