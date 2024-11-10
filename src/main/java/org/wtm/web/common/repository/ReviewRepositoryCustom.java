package org.wtm.web.common.repository;

import org.wtm.web.review.dto.ReviewListDto;
import org.wtm.web.review.model.Review;
import java.util.List;

public interface ReviewRepositoryCustom {
//    List<Review> findAllByStoreIdWithSorting(Long storeId, String sortOption);

    List<ReviewListDto> findAllByStoreIdWithSorting(Long storeId, String sortOption);
}