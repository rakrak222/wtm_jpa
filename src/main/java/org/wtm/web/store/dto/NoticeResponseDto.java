package org.wtm.web.store.dto;

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
    private LocalDateTime createdTime; // 작성 날짜
}
