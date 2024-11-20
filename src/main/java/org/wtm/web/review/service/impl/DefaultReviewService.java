package org.wtm.web.review.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.common.repository.*;
import org.wtm.web.common.service.UploadService;
import org.wtm.web.menu.model.MenuImg;
import org.wtm.web.review.dto.*;
import org.wtm.web.review.mapper.ReviewCountMapper;
import org.wtm.web.review.mapper.ReviewMapper;
import org.wtm.web.review.mapper.ReviewScoreMapper;
import org.wtm.web.review.mapper.ReviewStatsMapper;
import org.wtm.web.review.model.*;
import org.wtm.web.review.service.ReviewService;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.TicketHistoryUsage;
import org.wtm.web.user.model.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewScoreMapper reviewScoreMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewScoreRepository reviewScoreRepository;
    private final ReviewScaleRepository reviewScaleRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UploadService uploadService;
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    private final ReviewRepositoryCustom reviewRepositoryCustom;
    private final TicketHistoryUsageRepository ticketHistoryUsageRepository;


    @Value("${image.upload-review-dir}")
    private String uploadDir;

    @Transactional
    public ReviewStatsDto getReviewStats(Long storeId) {

        // 전체 평균 점수
        Double overallAverageScore = reviewRepository.findOverallAverageScoreByStoreId(storeId);

        // ReviewScale 별 평균 점수
        List<Object[]> scaleAverages = reviewRepository.findAverageScoreByReviewScaleAndStoreId(storeId);

        // Mapper를 사용하여 DTO 생성
        return ReviewStatsMapper.toDto(overallAverageScore, scaleAverages);

    }


    @Override
    public ReviewCountDto getReviewCount(long storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 Store를 찾을수 없습니다."));
        // 리뷰 개수
        long reviewCount = reviewRepository.findReviewCountByStoreId(storeId);

        return ReviewCountMapper.toDto(reviewCount);
    }

    @Override
    public void addReviewLike(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        ReviewLike reviewLike = ReviewLike.builder()
                .review(review)
                .user(user)
                .build();

        reviewLikeRepository.save(reviewLike);
    }

    @Override
    public void removeReviewLike(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        ReviewLike reviewLike = reviewLikeRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new IllegalArgumentException("리뷰 Like가 존재하지 않습니다."));

                reviewLikeRepository.delete(reviewLike);

    }


    @Override
    @Transactional(readOnly = true)
    public Slice<ReviewListDto> getReviewsByStoreId(Long storeId, String sortOption, Pageable pageable, String username) {

        Long userId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElse(0L);
        System.out.println("userId = " + userId);
        Slice<ReviewListDto> reviews = reviewRepositoryCustom.findAllByStoreIdWithSorting(storeId, sortOption, pageable, userId);

        reviews.forEach(review -> {
            // 리뷰의 relativeDate 계산
            review.setRelativeDate(calculateRelativeDate(review.getReviewRegDate()));

            // 댓글 리스트에 대해 relativeDate 계산
            review.getReviewComments().forEach(comment -> {
                comment.setRelativeDate(calculateRelativeDate(comment.getCommentRegDate()));
            });
        });

        return reviews;
    }

    private String calculateRelativeDate(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(date, now);

        if (duration.toMinutes() < 60) {
            return duration.toMinutes() + "분 전";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "시간 전";
        } else if (duration.toDays() < 7) {
            return duration.toDays() + "일 전";
        } else if (duration.toDays() < 30) {
            return "몇 주 전";
        } else if (duration.toDays() < 365) {
            return "몇 달 전";
        } else {
            return "1년 이상";
        }
    }



    @Override
    @Transactional
    public void addReview(Long storeId, Long ticketHistoryUsageId, ReviewRequestDto reviewRequestDto, List<MultipartFile> files, String username) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 Store를 찾을 수 없습니다."));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("해당 Email의 User를 찾을 수 없습니다: " + username));
        TicketHistoryUsage ticketHistoryUsage = ticketHistoryUsageRepository.findById(ticketHistoryUsageId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 TicketHistoryUsage를 찾을 수 없습니다."));

        // 리뷰 내용 및 재방문 여부 등록
        Review review = reviewMapper.toEntity(
                reviewRequestDto.getReviewContent(),
                reviewRequestDto.isRevisit(),
                store,
                ticketHistoryUsage,
                user
        );
        review = reviewRepository.save(review);

        // 리뷰 점수 등록
        List<ReviewScoreDto> scores = reviewRequestDto.getReviewScoresDtos();
        for (ReviewScoreDto score : scores) {
            ReviewScale reviewScale = reviewScaleRepository.findById(score.getReviewScaleId())
                    .orElseThrow(() -> new NoSuchElementException("ReviewScale ID " + score.getReviewScaleId() + " not found"));
            ReviewScore reviewScore = reviewScoreMapper.toEntity(score.getReviewScore(), reviewScale, review);
            reviewScoreRepository.save(reviewScore);
        }

        // 리뷰 사진 등록
        if (files != null && !files.isEmpty()) {
            List<String> images = uploadService.uploadFiles(files, uploadDir);
            List<ReviewImg> reviewImgs = new ArrayList<>();
            for (String image : images) {
                ReviewImg reviewImg = ReviewImg.builder()
                        .img(image)
                        .review(review)
                        .build();
                reviewImgs.add(reviewImg);
            }
            reviewImgRepository.saveAll(reviewImgs);
        }
    }

}
