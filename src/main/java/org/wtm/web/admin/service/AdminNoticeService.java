package org.wtm.web.admin.service;

import org.springframework.data.domain.Pageable;
import org.wtm.web.admin.dto.notice.*;
import org.wtm.web.store.model.Notice;

import java.util.List;

public interface AdminNoticeService {

    /**
     * 공지사항 조회
     */
    public NoticePageResponse getNoticesByStoreId(Long storeId, Pageable pageable);

    /**
     * 공지사항 등록
     */
    public NoticeDto createNotice(Long storeId, NoticeCreateDto noticeCreateDto);

    /**
     * 공지사항 수정
     */
    public NoticeDto updateNotice(Long storeId, Long noticeId, NoticeUpdateDto noticeUpdateDto);

    /**
     * 공지사항 삭제
     */
    public void deleteNotice(Long storeId, Long noticeId);

    /**
     * 특정 공지 수정을 위한 조회
     */
    NoticeDto getNoticeByStoreId(Long storeId, Long noticeId);
}
