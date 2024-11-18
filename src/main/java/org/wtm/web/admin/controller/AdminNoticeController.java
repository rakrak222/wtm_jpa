package org.wtm.web.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.admin.dto.notice.*;
import org.wtm.web.admin.service.AdminNoticeService;
import org.wtm.web.store.model.Notice;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @GetMapping("/stores/{storeId}/notices")
    public ResponseEntity<NoticePageResponse> getNotices(@PathVariable Long storeId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size){
        try {
            Pageable pageable = PageRequest.of(page, size);
            NoticePageResponse response = adminNoticeService.getNoticesByStoreId(storeId, pageable);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/stores/{storeId}/notices/{noticeId}")
    public ResponseEntity<NoticeDto> getNotices(@PathVariable Long storeId, @PathVariable Long noticeId){
        try {
            NoticeDto notice = adminNoticeService.getNoticeByStoreId(storeId, noticeId);
            return new ResponseEntity<>(notice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/stores/{storeId}/notices")
    public ResponseEntity<?> createNotice(@PathVariable Long storeId, @RequestBody NoticeCreateDto noticeCreateDto){
        try {
            NoticeDto noticeDto = adminNoticeService.createNotice(storeId, noticeCreateDto);
            return new ResponseEntity<>(noticeDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/stores/{storeId}/notices/{noticeId}")
    public ResponseEntity<?> updateNotice(
            @PathVariable Long storeId,
            @PathVariable Long noticeId,
            @RequestBody NoticeUpdateDto noticeUpdateDto) {
        try {
            NoticeDto updatedNotice = adminNoticeService.updateNotice(storeId, noticeId, noticeUpdateDto);
            return new ResponseEntity<>(updatedNotice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/stores/{storeId}/notices/{noticeId}")
    public ResponseEntity<?> deleteNotice(
            @PathVariable Long storeId,
            @PathVariable Long noticeId) {
        try {
            adminNoticeService.deleteNotice(storeId, noticeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
