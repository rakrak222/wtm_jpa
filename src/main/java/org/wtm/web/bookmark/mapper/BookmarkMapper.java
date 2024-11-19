package org.wtm.web.bookmark.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.bookmark.dto.BookmarkResponseDto;
import org.wtm.web.store.model.Store;

@Component
public class BookmarkMapper {
    public BookmarkResponseDto toBookmarkResponseDto(Store store, boolean isBookmarked) {
        return new BookmarkResponseDto(
                store.getId(),
                isBookmarked
        );
    }
}
