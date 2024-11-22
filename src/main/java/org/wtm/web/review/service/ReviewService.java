package org.wtm.web.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.review.dto.*;

import java.util.List;

public interface ReviewService {

    ReviewStatsDto getReviewStats(Long storeId);

    void addReview(Long storeId, Long ticketHistoryUsageId, ReviewRequestDto reviewRequestDto, List<MultipartFile> files, String username);


    Slice<ReviewListDto> getReviewsByStoreId(Long storeId, String sortOption, Pageable pageable, String username);

    ReviewCountDto getReviewCount(long storeId);



    void addReviewLike(Long reviewId, String username);

    void removeReviewLike(Long reviewId, String username);
}
