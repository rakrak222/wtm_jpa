package org.wtm.web.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
    private String storeName; // 가게 이름
    private String title; // 공지사항 제목
    private String content; // 공지사항 내용
    private LocalDateTime regDate; // 작성 날짜
    private String profilePicture; // 작성자의 프로필 사진 URL
    private String timeAgo; // 상대 시간 표시 필드
}
