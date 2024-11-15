package org.wtm.web.store.dto;


import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import org.wtm.web.auth.dto.Address;
import org.wtm.web.store.model.Store;
import org.wtm.web.review.model.ReviewScore;

@Data
@Builder
public class StoreResponseDto {

    private Long storeId;
    private String name;
    private String operatingHours;
    private Long price;
    private Boolean isBookmarked;
    private String img;
    private Double rating;

    @Embedded
    private Address address;

//    // Store 엔티티를 받아서 필요한 데이터로 변환
//    public StoreResponseDto(Store store) {
//        this.storeId = store.getId();
//        this.name = store.getName();
//        this.address = store.getAddress();
//        this.operatingHours = store.getOpenTime() + " - " + store.getCloseTime();
//        this.price = store.getTickets().isEmpty() ? null : store.getTickets().get(0).getPrice();
//        this.isBookmarked = !store.getBookmarks().isEmpty(); // 북마크가 존재하는지 여부
//        this.img = store.getUser().getProfilePicture();
//        this.rating = store.getReviews().stream()
//                .flatMap(review -> review.getReviewScores().stream())
//                .mapToDouble(ReviewScore::getScore)
//                .average()
//                .orElse(0.0); // 리뷰 점수 평균 계산
//    }
}
