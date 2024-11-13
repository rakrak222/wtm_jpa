package org.wtm.web.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.review.dto.*;

import java.util.List;

public interface ReviewService {

    ReviewStatsDto getReviewStats(Long storeId);

    void addReview(Long storeId, ReviewRequestDto reviewRequestDto, List<MultipartFile> files, Long userId);


    Slice<ReviewListDto> getReviewsByStoreId(Long storeId, String sortOption, Pageable pageable);

    ReviewCountDto getReviewCount(long storeId);
}
