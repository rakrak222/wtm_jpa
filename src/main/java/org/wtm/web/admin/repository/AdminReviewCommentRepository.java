package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.review.model.ReviewComment;

public interface AdminReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

}
