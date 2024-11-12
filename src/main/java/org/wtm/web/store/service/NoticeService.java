package org.wtm.web.store.service;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.wtm.web.menu.dto.NoticeResponseDto;

import java.util.List;

public interface NoticeService {
    Slice<NoticeResponseDto> getNoticesByStoreId(Long storeId, Pageable pageable);
}
