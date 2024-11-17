package org.wtm.web.bookmark.service;


import org.wtm.web.bookmark.dto.BookmarkResponseDto;

public interface BookmarkService {

    void addBookmark(Long storeId);
    void removeBookmark(Long storeId);

    BookmarkResponseDto getBookmarkStatus(Long storeId, Long userId);
}
