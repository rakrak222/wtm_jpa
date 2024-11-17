package org.wtm.web.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.review.dto.*;

import java.util.List;

public interface ReviewService {

    ReviewStatsDto getReviewStats(Long storeId);

    void addReview(Long storeId, Long ticketHistoryUsageId, ReviewRequestDto reviewRequestDto, List<MultipartFile> files, Long userId);


    Slice<ReviewListDto> getReviewsByStoreId(Long storeId, String sortOption, Pageable pageable, Long UserId);

    ReviewCountDto getReviewCount(long storeId);



    void addReviewLike(Long reviewId, Long fixedUserId);

    void removeReviewLike(Long reviewId, Long fixedUserId);
}
