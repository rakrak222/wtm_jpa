package org.wtm.web.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.menu.dto.NoticeResponseDto;
import org.wtm.web.store.dto.StoreDetailResponseDto;
import org.wtm.web.store.dto.StoreResponseDto;
import org.wtm.web.store.dto.StoreReviewStatsDto;
import org.wtm.web.store.service.NoticeService;
import org.wtm.web.store.service.StoreService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final NoticeService noticeService;


    // 전체 식당 조회(검색 기능 없는 버전)
//    @GetMapping
    public ResponseEntity<?> getAllStores() {
        List<StoreResponseDto> stores = storeService.getAllStores();
        if (stores.isEmpty()) {
            return ResponseEntity.status(404).body("식당 목록이 없습니다.");
        }
        return ResponseEntity.ok(stores);
    }

    // 전체 식당 조회(검색 기능 포함 버전)
    @GetMapping
    public ResponseEntity<?> getStores(@RequestParam(required = false) String query) {
        List<StoreResponseDto> stores = storeService.getStores(query);

        if (stores.isEmpty()) {
            return ResponseEntity.status(404).body("식당 목록이 없습니다.");
        }
        return ResponseEntity.ok(stores);
    }

    // 특정 가게 리뷰 요약 정보 조회

    @GetMapping("/{storeId}/review-summary")
    public ResponseEntity<?> getStoreReviewStats(@PathVariable("storeId") long storeId) {
        StoreReviewStatsDto storeStats = storeService.getStoreReviewStats(storeId);
        if (storeStats == null) {
            return ResponseEntity.status(404).body("해당 가게를 찾을수 없습니다.");
        }
        return ResponseEntity.ok(storeStats);
    }

    // 특정 가게 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable Long id) {
        StoreDetailResponseDto storeDetails = storeService.getStoreDetailsById(id);
        if (storeDetails == null) {
            return ResponseEntity.status(404).body("식당이 조회되지 않습니다.");
        }
        return ResponseEntity.ok(storeDetails);
    }

    @GetMapping("/{storeId}/notices")
    public ResponseEntity<?> getNoticesByStoreId(
            @PathVariable Long storeId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page, size);
        Slice<NoticeResponseDto> response = noticeService.getNoticesByStoreId(storeId, pageable);
        if (response == null || response.isEmpty()) {
            return ResponseEntity.status(404).body("공지사항을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }
}
