package org.wtm.web.user.service;

import org.springframework.stereotype.Service;
import org.wtm.web.user.dto.UserResponseDto;
import org.wtm.web.user.dto.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.review.UserReviewDto;
import org.wtm.web.user.dto.ticket.*;

import java.util.List;

@Service
public interface MyPageService {

    UserResponseDto getMyPage(Long id);

    UserResponseDto getMySettings(Long id);

    boolean updateMySettings(UserUpdateDto userUpdateDto);

    List<TicketSummaryDto> getTicketsOwnedByUser(Long id);

    TicketHistoryResponseDto getMyTicketHistory(Long userId, int month, int year, int page, int size);
//    TicketHistoryResponseDto getMyTicketHistory(Long userId, int month, int year);

    TicketHistoryResponseDto getMyTicketHistoryByStore(Long userId, Long storeId, int month, int year);

    List<UserReviewDto> getMyReviews(Long userId);

    List<BookmarkDto> getMyBookmarks(Long userId);

    boolean deleteMyReview(Long reviewId, Long userId);

    boolean saveMyBookmark(Long storeId, Long userId);

    boolean deleteMyBookmark(Long storeId, Long userId);

    TicketDto getMyTicketDetail(Long storeId, Long userId);
}
