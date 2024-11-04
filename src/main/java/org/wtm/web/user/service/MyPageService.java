package org.wtm.web.user.service;

import org.springframework.stereotype.Service;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.review.model.Review;
import org.wtm.web.ticket.model.Ticket;
import org.wtm.web.user.dto.UserResponseDto;
import org.wtm.web.user.dto.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.review.UserReviewDto;
import org.wtm.web.user.dto.ticket.TicketAllHistoryDto;
import org.wtm.web.user.dto.ticket.TicketPurchaseDto;
import org.wtm.web.user.dto.ticket.TicketSummaryDto;
import org.wtm.web.user.dto.ticket.TicketUsageDto;

import java.util.List;

@Service
public interface MyPageService {

    UserResponseDto getMyPage(Long id);

    UserResponseDto getMySettings(Long id);

    boolean updateMySettings(UserUpdateDto userUpdateDto);

    List<TicketSummaryDto> getTicketsOwnedByUser(Long id);

    boolean useMyTicket(TicketUsageDto ticketUsageDto);

    boolean purchaseMyTicket(TicketPurchaseDto ticketPurchaseDto);

    List<TicketAllHistoryDto> getMyTicketHistory(Long userId, int month, int year);

    List<TicketAllHistoryDto> getMyTicketHistoryByStore(Long userId, Long storeId, int month, int year);

    List<UserReviewDto> getMyReviews(Long userId);

    List<BookmarkDto> getMyBookmarks(Long userId);

    boolean deleteMyReview(Long reviewId);
}
