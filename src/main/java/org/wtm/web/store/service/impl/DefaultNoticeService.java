package org.wtm.web.store.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtm.web.common.repository.NoticeRepository;
import org.wtm.web.menu.dto.NoticeResponseDto;
import org.wtm.web.store.mapper.NoticeMapper;
import org.wtm.web.store.model.Notice;
import org.wtm.web.store.service.NoticeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultNoticeService implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    public List<NoticeResponseDto> getNoticesByStoreId(Long storeId) {
        List<Notice> notices = noticeRepository.findByStoreId(storeId);
        return notices.stream()
                .map(noticeMapper::toDto)  // NoticeMapper를 사용하여 변환
                .collect(Collectors.toList());
    }
}
