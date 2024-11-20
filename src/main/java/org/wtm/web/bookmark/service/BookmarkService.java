package org.wtm.web.bookmark.service;


import org.wtm.web.bookmark.dto.BookmarkResponseDto;

public interface BookmarkService {

    void addBookmark(Long storeId, String username);
    void removeBookmark(Long storeId, String username);

    BookmarkResponseDto getBookmarkStatus(Long storeId, String username);
}
