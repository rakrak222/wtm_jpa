package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.review.model.ReviewImg;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
}
