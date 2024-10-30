package org.wtm.web.bookmark.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.bookmark.service.BookmarkService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<?> addBookmark(@RequestParam Long storeId) {
        bookmarkService.addBookmark(storeId);
        return ResponseEntity.ok("북마크가 추가되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<?> removeBookmark(@RequestParam Long storeId) {
        bookmarkService.removeBookmark(storeId);
        return ResponseEntity.ok("북마크가 삭제되었습니다.");
    }
}
