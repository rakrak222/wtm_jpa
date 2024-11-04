package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.review.model.ReviewScale;

@Repository
public interface ReviewScaleRepository extends JpaRepository<ReviewScale, Long> {
}
