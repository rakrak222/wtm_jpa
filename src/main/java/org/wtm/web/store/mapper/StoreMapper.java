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

        return StoreResponseDto.builder()
                .storeId(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .operatingHours(store.getOpenTime() + " - " + store.getCloseTime())
                .price(store.getTickets().isEmpty() ? null : store.getTickets().get(0).getPrice())
                .isBookmarked(!store.getBookmarks().isEmpty())
                .img(store.getUser().getProfilePicture())
                .rating(rating)
                .build();
    }
}
