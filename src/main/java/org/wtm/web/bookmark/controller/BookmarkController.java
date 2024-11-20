package org.wtm.web.bookmark.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.bookmark.dto.BookmarkResponseDto;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.bookmark.service.BookmarkService;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;



    @GetMapping("{storeId}/bookmark")
    public ResponseEntity<?> getBookmark(
            @PathVariable Long storeId,
            @RequestHeader("x-username") String username) {

        Long userId = 1L; // 테스트를 위해 userId를 2로 고정

        BookmarkResponseDto bookmarkResponseDto = bookmarkService.getBookmarkStatus(storeId, username);

        if (bookmarkResponseDto == null) {
            return ResponseEntity.status(404).body("해당 가게를 찾을수 없습니다.");
        }
        return ResponseEntity.ok(bookmarkResponseDto);
    }

    @PostMapping("{storeId}/bookmark")
    public ResponseEntity<?> addBookmark(@PathVariable Long storeId, @RequestHeader("x-username") String username) {

        System.out.println("username = " + username);
        bookmarkService.addBookmark(storeId, username);
        return ResponseEntity.ok("북마크가 추가되었습니다.");
    }

    @DeleteMapping("{storeId}/bookmark")
    public ResponseEntity<?> removeBookmark(@PathVariable Long storeId, @RequestHeader("x-username") String username) {
        bookmarkService.removeBookmark(storeId, username);
        return ResponseEntity.ok("북마크가 삭제되었습니다.");
    }
}
