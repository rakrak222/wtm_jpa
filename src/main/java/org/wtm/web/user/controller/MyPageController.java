package org.wtm.web.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.user.dto.review.ReviewPageResponse;
import org.wtm.web.user.dto.user.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.review.UserReviewDto;
import org.wtm.web.user.dto.ticket.*;
import org.wtm.web.user.service.MyPageService;
import org.wtm.web.user.dto.user.UserResponseDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/my")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 마이페이지 조회
    @GetMapping
    public ResponseEntity<UserResponseDto> getMyPage(@RequestParam("username") String username) {
        UserResponseDto userResponseDto = myPageService.getMyPage(username);

        if (userResponseDto != null) {
            return ResponseEntity.ok(userResponseDto); // 200 OK와 함께 데이터 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
        }
    }

    // 사용자 정보 불러오기
    @GetMapping("/settings")
    public ResponseEntity<UserResponseDto> getMySettings(@RequestParam("username") String username) {
        UserResponseDto userSettings = myPageService.getMySettings(username);

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
    public ResponseEntity<List<TicketSummaryDto>> getTicketsOwnedByUser(@RequestParam("username") String username) {
        List<TicketSummaryDto> ticketListInfo = myPageService.getTicketsOwnedByUser(username);

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
            @RequestParam("username") String username
    ) {
        TicketDto ticketDto = myPageService.getMyTicketDetail(storeId, username);
        if (ticketDto!=null){
            return ResponseEntity.ok().body(ticketDto); // 200 OK와 함께 데이터 반환
        }
        System.out.println("help");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 사용자의 모든 티켓 구매 사용 내역 조회
    @GetMapping("/tickets/history")
    public ResponseEntity<TicketHistoryResponseDto> getMyTicketHistory(
            @RequestParam("username") String username,
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            @RequestParam("type") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        TicketHistoryResponseDto ticketHistoryResponseDto = myPageService.getMyTicketHistory(username, month, year, type, pageable);

        if (ticketHistoryResponseDto != null){
            return ResponseEntity.ok(ticketHistoryResponseDto); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 사용자의 특정 가게 티켓 구매 사용 내역 조회
    @GetMapping("/tickets/stores/history")
    public ResponseEntity<TicketHistoryResponseDto> getMyTicketHistoryByStore(
            @RequestParam("username") String username,
            @RequestParam("storeId") Long storeId,
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            @RequestParam("type") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        TicketHistoryResponseDto ticketHistoryResponseDto = myPageService.getMyTicketHistoryByStore(username, storeId, month, year, type, pageable);
        if (ticketHistoryResponseDto != null){
            return ResponseEntity.ok(ticketHistoryResponseDto); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 본인 리뷰 목록 조회
    @GetMapping("/reviews/{userId}")
    public ResponseEntity<ReviewPageResponse> getMyReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size){
        try {
            Pageable pageable = PageRequest.of(page, size);
            ReviewPageResponse response = myPageService.getMyReviews(userId, pageable);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }}


    // 사용자가 본인 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}/userIds/{userId}")
    public ResponseEntity<String> deleteMyReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId
    ) {
        boolean result = myPageService.deleteMyReview(reviewId, userId);

        if (result) {
            return ResponseEntity.ok().body("Successfully delete review"); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    // 사용자 개인 북마크 조회
    @GetMapping("/bookmarks")
    public ResponseEntity<List<BookmarkDto>> getMyBookmarks(@RequestParam("username") String username) {
        List<BookmarkDto> bookmarkDto = myPageService.getMyBookmarks(username);
        if (bookmarkDto != null){
            return ResponseEntity.ok(bookmarkDto); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    @PostMapping("/bookmarks")
    public ResponseEntity<String> saveMyBookmark(@RequestBody BookmarkDto bookmarkDto) {
        boolean result = myPageService.saveMyBookmark(bookmarkDto);

        if (result) {
            return ResponseEntity.ok().body("Successfully add bookmark"); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }

    @DeleteMapping("/bookmarks/stores/{storeId}/users/{username}")
    public ResponseEntity<String> deleteMyBookmark(
            @PathVariable Long storeId,
            @PathVariable String username
    ) {
        boolean result = myPageService.deleteMyBookmark(storeId, username);

        if (result) {
            return ResponseEntity.ok().body("Successfully delete bookmark"); // 200 OK와 함께 데이터 반환
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found 반환
    }


}

