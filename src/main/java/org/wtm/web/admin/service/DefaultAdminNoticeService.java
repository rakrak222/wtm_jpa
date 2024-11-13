package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.admin.dto.notice.*;
import org.wtm.web.admin.mapper.AdminNoticeMapper;
import org.wtm.web.admin.repository.AdminNoticeRepository;
import org.wtm.web.admin.repository.AdminReviewRepository;
import org.wtm.web.admin.repository.AdminStoreRepository;
import org.wtm.web.admin.repository.AdminUserRepository;
import org.wtm.web.store.model.Notice;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAdminNoticeService implements AdminNoticeService {

    private final AdminNoticeRepository noticeRepository;
    private final AdminStoreRepository storeRepository;
    private final AdminUserRepository userRepository;
    private final AdminNoticeMapper noticeMapper;


    /**
     * 공지 조회
     */
    @Override
    public NoticePageResponse getNoticesByStoreId(Long storeId, Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findNoticesWithUserByStoreId(storeId, pageable);
        List<NoticeListDto> noticeList = noticePage.stream()
                .map(noticeMapper::toNoticeListDto)
                .collect(Collectors.toList());
        return NoticePageResponse.builder()
                .notices(noticeList)
                .currentPage(noticePage.getNumber())
                .totalPages(noticePage.getTotalPages())
                .totalItems(noticePage.getTotalElements())
                .build();
    }

    /**
     * 공지 작성
     */
    @Override
    public NoticeDto createNotice(Long storeId, NoticeCreateDto noticeCreateDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));
        User user = userRepository.findById(noticeCreateDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Notice notice = noticeMapper.toNoticeEntity(noticeCreateDto, store, user);
        Notice savedNotice = noticeRepository.save(notice);
        return noticeMapper.toNoticeDto(savedNotice);
    }

    /**
     * 공지 수정
     */
    @Override
    public NoticeDto updateNotice(Long storeId, Long noticeId, NoticeUpdateDto noticeUpdateDto) {
        Notice notice = noticeRepository.findNoticeByStoreIdAndId(storeId, noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        noticeMapper.updateNoticeFromDto(noticeUpdateDto, notice);
        Notice updatedNotice = noticeRepository.save(notice);
        return noticeMapper.toNoticeDto(updatedNotice);
    }

    /**
     * 공지 삭제
     */
    @Override
    public void deleteNotice(Long storeId, Long noticeId) {
        Notice notice = noticeRepository.findNoticeByStoreIdAndId(storeId, noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        noticeRepository.delete(notice);
    }

    /**
     * 특정 공지 수정을 위한 조회
     */
    @Override
    public NoticeDto getNoticeByStoreId(Long storeId, Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        return noticeMapper.toNoticeDto(notice);
    }
}
