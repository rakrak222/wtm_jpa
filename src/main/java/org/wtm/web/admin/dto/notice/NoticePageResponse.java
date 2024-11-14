package org.wtm.web.admin.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticePageResponse {
    List<NoticeListDto> notices;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
