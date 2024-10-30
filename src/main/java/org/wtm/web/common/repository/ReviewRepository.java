package org.wtm.web.common.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.review.model.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("SELECT COALESCE(AVG(rs.score),0) FROM ReviewScore rs WHERE rs.review.store.id = :storeId")
    Double findOverallAverageScoreByStoreId(@Param("storeId") Long storeId);

    // ReviewScale 별 평균 리뷰 점수
    @Query("SELECT rs.reviewScale.name, COALESCE(AVG(rs.score), 0) " +
            "FROM ReviewScore rs WHERE rs.review.store.id = :storeId " +
            "GROUP BY rs.reviewScale.name")
    List<Object[]> findAverageScoreByReviewScaleAndStoreId(@Param("storeId") Long storeId);


    List<Review> findAllByStoreId(Long storeId);
}
