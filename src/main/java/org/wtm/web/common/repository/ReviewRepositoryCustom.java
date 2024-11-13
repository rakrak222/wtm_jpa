package org.wtm.web.common.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.wtm.web.review.dto.ReviewListDto;
import org.wtm.web.review.model.Review;
import java.util.List;

public interface ReviewRepositoryCustom {
//    List<Review> findAllByStoreIdWithSorting(Long storeId, String sortOption);

    Slice<ReviewListDto> findAllByStoreIdWithSorting(Long storeId, String sortOption, Pageable pageable);
}