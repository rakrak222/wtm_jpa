package org.wtm.web.common.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.review.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
