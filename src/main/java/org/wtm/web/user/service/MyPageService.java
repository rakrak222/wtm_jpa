package org.wtm.web.user.service;

import org.springframework.stereotype.Service;
import org.wtm.web.user.dto.user.UserResponseDto;
import org.wtm.web.user.dto.user.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.review.UserReviewDto;
import org.wtm.web.user.dto.ticket.*;

import java.util.List;

@Service
public interface MyPageService {

    UserResponseDto getMyPage(String username);

    UserResponseDto getMySettings(String username);

    boolean updateMySettings(UserUpdateDto userUpdateDto);

    List<TicketSummaryDto> getTicketsOwnedByUser(String username);

    TicketHistoryResponseDto getMyTicketHistory(String username, int month, int year, int page, int size);

    TicketHistoryResponseDto getMyTicketHistoryByStore(String username, Long storeId, int month, int year);

    List<UserReviewDto> getMyReviews(String username);

    List<BookmarkDto> getMyBookmarks(String username);

    boolean deleteMyReview(Long reviewId, String username);

    boolean saveMyBookmark(Long storeId, String username);

    boolean deleteMyBookmark(Long storeId, String username);

    TicketDto getMyTicketDetail(Long storeId, String username);
}
