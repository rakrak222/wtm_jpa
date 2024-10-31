package org.wtm.web.store.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.menu.dto.NoticeResponseDto;
import org.wtm.web.store.model.Notice;

@Component
public class NoticeMapper {

    // Notice 엔티티를 NoticeResponseDto로 변환하는 메서드 (Builder 패턴 사용)
    public NoticeResponseDto toDto(Notice notice) {
        return NoticeResponseDto.builder()
                .storeName(notice.getStore().getName())
                .title(notice.getTitle())
                .content(notice.getContent())
                .regDate(notice.getRegDate())
                .build();
    }
}
