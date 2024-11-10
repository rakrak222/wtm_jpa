package org.wtm.web.review.service;

import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.review.dto.*;

import java.util.List;

public interface ReviewService {

    ReviewStatsDto getReviewStats(Long storeId);

    void addReview(Long storeId, ReviewRequestDto reviewRequestDto, List<MultipartFile> files, Long userId);


    List<ReviewListDto> getReviewsByStoreId(Long storeId, String sortOption);

    ReviewCountDto getReviewCount(long storeId);
}
