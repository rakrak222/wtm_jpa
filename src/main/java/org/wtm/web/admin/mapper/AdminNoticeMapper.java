package org.wtm.web.admin.mapper;


import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.notice.NoticeCreateDto;
import org.wtm.web.admin.dto.notice.NoticeDto;
import org.wtm.web.admin.dto.notice.NoticeListDto;
import org.wtm.web.admin.dto.notice.NoticeUpdateDto;
import org.wtm.web.store.model.Notice;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

@Component
public class AdminNoticeMapper {

    public NoticeDto toNoticeDto(Notice notice) {
        return new NoticeDto(
                notice.getId(),
                notice.getStore().getId(),
                notice.getUser().getId(),
                notice.getTitle(),
                notice.getContent()
        );
    }

    public NoticeListDto toNoticeListDto(Notice notice) {
        return new NoticeListDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getUser().getName(),
                notice.getUser().getProfilePicture(),
                notice.getRegDate()
        );
    }

    public Notice toNoticeEntity(NoticeCreateDto noticeCreateDto, Store store, User user) {
        return Notice.builder()
                .store(store)
                .user(user)
                .title(noticeCreateDto.getTitle())
                .content(noticeCreateDto.getContent())
                .build();
    }

    public void updateNoticeFromDto(NoticeUpdateDto noticeUpdateDto, Notice notice) {
        notice.changeTitle(
                noticeUpdateDto.getTitle()
        );
        notice.changeContent(
                noticeUpdateDto.getContent()
        );
    }
}
