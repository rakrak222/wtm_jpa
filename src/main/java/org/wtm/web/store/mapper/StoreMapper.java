package org.wtm.web.store.mapper;

import org.wtm.web.store.dto.StoreResponseDto;
import org.wtm.web.store.model.Store;
import org.wtm.web.review.model.ReviewScore;

public class StoreMapper {
    public static StoreResponseDto toDto(Store store) {
        Double rating = store.getReviews().stream()
                .flatMap(review -> review.getReviewScores().stream())
                .mapToDouble(ReviewScore::getScore)
                .average()
                .orElse(0.0);

        // 소수 첫째 자리까지 반올림
        rating = Math.round(rating * 10) / 10.0;

        // 하드코딩된 userId
        Long userId = 1L;

        boolean isBookmarked = store.getBookmarks().stream()
                .anyMatch(bookmark -> bookmark.getUser().getId().equals(userId));

        return StoreResponseDto.builder()
                .storeId(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .operatingHours(store.getOpenTime() + " - " + store.getCloseTime())
                .price(store.getTickets().isEmpty() ? null : store.getTickets().get(0).getPrice())
                .isBookmarked(isBookmarked)
                .img(store.getUser().getProfilePicture())
                .rating(rating)
                .build();
    }
}
