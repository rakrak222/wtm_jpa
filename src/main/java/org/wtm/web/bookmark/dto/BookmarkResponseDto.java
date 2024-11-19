package org.wtm.web.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long storeId;
    private Boolean isBookmarked;

}
