package org.wtm.web.admin.service;

import org.wtm.web.admin.dto.notice.NoticeCreateDto;
import org.wtm.web.admin.dto.notice.NoticeDto;
import org.wtm.web.admin.dto.notice.NoticeListDto;
import org.wtm.web.admin.dto.notice.NoticeUpdateDto;
import org.wtm.web.store.model.Notice;

import java.util.List;

public interface AdminNoticeService {

    /**
     * 공지사항 조회
     */
    public List<NoticeListDto> getNoticesByStoreId(Long storeId);

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

}
