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
import org.wtm.web.review.model.Review;
import org.wtm.web.review.model.ReviewImg;
import org.wtm.web.review.model.ReviewScale;
import org.wtm.web.review.model.ReviewScore;
import org.wtm.web.review.service.ReviewService;
import org.wtm.web.store.model.Store;
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

    private final ReviewRepositoryCustom reviewRepositoryCustom;


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
    @Transactional(readOnly = true)
    public Slice<ReviewListDto> getReviewsByStoreId(Long storeId, String sortOption, Pageable pageable) {
        Slice<ReviewListDto> reviews = reviewRepositoryCustom.findAllByStoreIdWithSorting(storeId, sortOption, pageable);

        reviews.forEach(review -> {
            review.setRelativeDate(calculateRelativeDate(review.getReviewRegDate()));

        });



        return reviews;
    }
    
    private String calculateRelativeDate(LocalDateTime reviewDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(reviewDate, now);
        
        if (duration.toMinutes() == 0) {
            return "오늘";
        } else if (duration.toDays() < 7) {
            return duration.toDays() + "일 전";
        } else if (duration.toDays() < 30) {
            return "일주일 전";
        } else if (duration.toDays() < 365) {
            return "한 달 전";
        } else {
            return "1년 전";
        }
    }




    @Override
    @Transactional
    public void addReview(Long storeId, ReviewRequestDto reviewRequestDto, List<MultipartFile> files, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 Store를 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 User를 찾을 수 없습니다: " + userId));

        // 리뷰 내용 및 재방문 여부 등록
        Review review = reviewMapper.toEntity(
                reviewRequestDto.getReviewContent(),
                reviewRequestDto.isRevisit(),
                store,
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
