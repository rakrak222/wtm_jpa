package org.wtm.web.store.service;


import org.wtm.web.menu.dto.NoticeResponseDto;

import java.util.List;

public interface NoticeService {
    List<NoticeResponseDto> getNoticesByStoreId(Long storeId);
}
