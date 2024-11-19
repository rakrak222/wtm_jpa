package org.wtm.web.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.user.dto.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.review.UserReviewDto;
import org.wtm.web.user.dto.ticket.*;
import org.wtm.web.user.service.MyPageService;
import org.wtm.web.user.dto.UserResponseDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/my")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 마이페이지 조회
    @GetMapping
    public ResponseEntity<UserResponseDto> getMyPage(@RequestParam("userId") Long id) {
        UserResponseDto userResponseDto = myPageService.getMyPage(id);

        if (userResponseDto != null) {
            return ResponseEntity.ok(userResponseDto); // 200 OK와 함께 데이터 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
        }
    }

    // 사용자 정보 불러오기
    @GetMapping("/settings")
    public ResponseEntity<UserResponseDto> getMySettings(@RequestParam("userId") Long id) {
        UserResponseDto userSettings = myPageService.getMySettings(id);

        if (userSettings != null) {
            return ResponseEntity.ok(userSettings); // 200 OK와 함께 데이터 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
        }
    }

    // 사용자 정보 업데이트
    @PutMapping(value = "/settings", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMySettings(@ModelAttribute UserUpdateDto userUpdateDto) {
        boolean result = myPageService.updateMySettings(userUpdateDto);

        if (result){
            return ResponseEntity.ok().body("User settings updated successfully"); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 사용자가 소유한 티켓의 가게 목록 조회
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketSummaryDto>> getTicketsOwnedByUser(@RequestParam("userId") Long id) {
        List<TicketSummaryDto> ticketListInfo = myPageService.getTicketsOwnedByUser(id);

        if (ticketListInfo != null) {
            return ResponseEntity.ok(ticketListInfo); // 200 OK와 함께 데이터 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
        }
    }
    // 티켓 상세 조회
    @GetMapping("/tickets/stores")
    public ResponseEntity<TicketDto> getMyTicketDetail(
            @RequestParam("storeId") Long storeId,
            @RequestParam("userId") Long userId
    ) {
        TicketDto ticketDto = myPageService.getMyTicketDetail(storeId, userId);
        if (ticketDto!=null){
            return ResponseEntity.ok().body(ticketDto); // 200 OK와 함께 데이터 반환
        }
        System.out.println("help");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 사용자의 모든 티켓 구매 사용 내역 조회
    @GetMapping("/tickets/history")
    public ResponseEntity<TicketHistoryResponseDto> getMyTicketHistory(
            @RequestParam("userId") Long userId,
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {

        TicketHistoryResponseDto ticketHistoryResponseDto = myPageService.getMyTicketHistory(userId, month, year, page, size);

        if (ticketHistoryResponseDto != null){
            return ResponseEntity.ok(ticketHistoryResponseDto); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 사용자의 특정 가게 티켓 구매 사용 내역 조회
    @GetMapping("/tickets/stores/history")
    public ResponseEntity<TicketHistoryResponseDto> getMyTicketHistoryByStore(
            @RequestParam("userId") Long userId,
            @RequestParam("storeId") Long storeId,
            @RequestParam("month") int month,
            @RequestParam("year") int year
    ) {
        TicketHistoryResponseDto ticketHistoryResponseDto = myPageService.getMyTicketHistoryByStore(userId, storeId, month, year);
        if (ticketHistoryResponseDto != null){
            return ResponseEntity.ok(ticketHistoryResponseDto); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 본인 리뷰 목록 조회
    @GetMapping("/reviews")
    public ResponseEntity<List<UserReviewDto>> getMyReviews(@RequestParam("userId") Long userId) {
        List<UserReviewDto> userReviewDto = myPageService.getMyReviews(userId);
        if (userReviewDto != null){
            return ResponseEntity.ok(userReviewDto); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환

    }

    // 사용자가 본인 리뷰 삭제
    @DeleteMapping("/reviews")
    public ResponseEntity<String> deleteMyReview(
            @RequestParam("reviewId") Long reviewId,
            @RequestParam("userId") Long userId
    ) {
        boolean result = myPageService.deleteMyReview(reviewId, userId);

        if (result) {
            return ResponseEntity.ok().body("Successfully delete review"); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 사용자 개인 북마크 조회
    @GetMapping("/bookmarks")
    public ResponseEntity<List<BookmarkDto>> getMyBookmarks(@RequestParam("userId") Long userId) {
        List<BookmarkDto> bookmarkDto = myPageService.getMyBookmarks(userId);
        if (bookmarkDto != null){
            return ResponseEntity.ok(bookmarkDto); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    @PostMapping("/bookmarks")
    public ResponseEntity<String> saveMyBookmark(
            @RequestParam("storeId") Long storeId,
            @RequestParam("userId") Long userId
            ) {
        boolean result = myPageService.saveMyBookmark(storeId, userId);

        if (result) {
            return ResponseEntity.ok().body("Successfully add bookmark"); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    @DeleteMapping("/bookmarks")
    public ResponseEntity<String> deleteMyBookmark(
            @RequestParam("storeId") Long storeId,
            @RequestParam("userId") Long userId
    ) {
        boolean result = myPageService.deleteMyBookmark(storeId, userId);

        if (result) {
            return ResponseEntity.ok().body("Successfully delete bookmark"); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }


}

