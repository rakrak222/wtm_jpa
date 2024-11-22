package org.wtm.web.user.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wtm.web.user.dto.review.ReviewPageResponse;
import org.wtm.web.user.dto.user.UserResponseDto;
import org.wtm.web.user.dto.user.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.ticket.*;

import java.util.List;

@Service
public interface MyPageService {

    UserResponseDto getMyPage(String username);

    UserResponseDto getMySettings(String username);

    boolean updateMySettings(UserUpdateDto userUpdateDto);

    List<TicketSummaryDto> getTicketsOwnedByUser(String username);

    TicketHistoryResponseDto getMyTicketHistory(String username, int month, int year, String type, Pageable pageable);

    TicketHistoryResponseDto getMyTicketHistoryByStore(String username, Long storeId, int month, int year, String type, Pageable pageable);

    ReviewPageResponse getMyReviews(Long userId, Pageable pageable);

    List<BookmarkDto> getMyBookmarks(String username);

    boolean deleteMyReview(Long reviewId, Long userId);

    boolean saveMyBookmark(BookmarkDto bookmarkDto);

    boolean deleteMyBookmark(Long storeId, String username);

    TicketDto getMyTicketDetail(Long storeId, String username);
}
