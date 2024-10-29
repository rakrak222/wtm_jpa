package org.wtm.web.admin.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoticeListDto {

    private Long noticeId;
    private String title;
    private String content;
    private String userName;
    private String userProfilePicture;
    private LocalDateTime noticeRegDate;


}
