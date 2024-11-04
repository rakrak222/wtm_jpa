package org.wtm.web.common.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.review.model.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(s.score)" +
            "FROM Review r " +
            "JOIN r.reviewScores s " +
            "WHERE r.store.id = :storeId ")
    double calculateAvgByStoreId(@Param("storeId") Long storeId);

    @EntityGraph(attributePaths = "store")
    List<Review> findByUserId(Long userId);

    Review findOneById(Long reviewId);
}
