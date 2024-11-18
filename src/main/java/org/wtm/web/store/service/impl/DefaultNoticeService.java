package org.wtm.web.store.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.wtm.web.common.repository.NoticeRepository;
import org.wtm.web.menu.dto.NoticeResponseDto;
import org.wtm.web.store.mapper.NoticeMapper;
import org.wtm.web.store.model.Notice;
import org.wtm.web.store.service.NoticeService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultNoticeService implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    @Override
    public Slice<NoticeResponseDto> getNoticesByStoreId(Long storeId, Pageable pageable) {
        return noticeRepository.findByStoreIdOrderByRegDateDesc(storeId, pageable)
                .map(notice -> {
                    NoticeResponseDto dto = noticeMapper.toDto(notice);
                    dto.setTimeAgo(getTimeAgo(notice.getRegDate()));
                    return dto;
                });
    }

    // 작성 날짜를 현재 시간과 비교해 상대 시간 형식으로 변환
    private String getTimeAgo(LocalDateTime regDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(regDate, now);

        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();

        if (days >= 365) {
            return (days / 365) + "년 전";
        } else if (days >= 30) {
            return (days / 30) + "달 전";
        } else if (days >= 7) {
            return (days / 7) + "주 전";
        } else if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else {
            return "방금 전";
        }
    }
}
