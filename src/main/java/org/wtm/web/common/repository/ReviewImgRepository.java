package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wtm.web.review.model.ReviewImg;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    @Query("SELECT r.img FROM ReviewImg r WHERE r.review.id=:id")
    String findImgByReviewId(@Param("id") Long id);
}
