package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.review.model.Review;

import java.util.List;

public interface AdminReviewRepository extends JpaRepository<Review, Long> {

//    @EntityGraph(attributePaths = {"user", "reviewComments", "reviewComments.user"})
//    List<Review> findAllByStoreId(Long storeId);

//    v1: 성능 신경 안쓴 기본 jpa n+1 문제 발생함.
    List<Review> findAllByStoreId(Long storeId);

}
