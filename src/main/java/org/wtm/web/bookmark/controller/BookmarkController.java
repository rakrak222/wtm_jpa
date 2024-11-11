package org.wtm.web.bookmark.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.bookmark.service.BookmarkService;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("{storeId}/bookmark")
    public ResponseEntity<?> addBookmark(@PathVariable Long storeId) {
        bookmarkService.addBookmark(storeId);
        return ResponseEntity.ok("북마크가 추가되었습니다.");
    }

    @DeleteMapping("{storeId}/bookmark")
    public ResponseEntity<?> removeBookmark(@PathVariable Long storeId) {
        bookmarkService.removeBookmark(storeId);
        return ResponseEntity.ok("북마크가 삭제되었습니다.");
    }
}
