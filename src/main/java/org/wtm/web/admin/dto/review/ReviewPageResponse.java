package org.wtm.web.admin.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPageResponse {
    private List<ReviewListDto> reviews;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
