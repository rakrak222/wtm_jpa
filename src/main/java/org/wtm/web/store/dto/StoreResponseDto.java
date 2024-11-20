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


}
