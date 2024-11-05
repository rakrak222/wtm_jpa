package org.wtm.web.common.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.review.model.ReviewScore;

import java.util.List;

@Repository
public interface ReviewScoreRepository extends JpaRepository<ReviewScore, Long> {
    // added by jwhuh 2024-11-04
    @Query("SELECT COALESCE(SUM(rs.score) / 4.0, 0) FROM ReviewScore rs WHERE rs.review.id = :reviewId")
    Double findAverageScoreByReviewId(@Param("reviewId") Long reviewId);

    List<ReviewScore> findByReviewId(Long id);
}