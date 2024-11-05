package org.wtm.web.admin.dto.info;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfoUpdateDto {
    private String profilePicture;
    private String storeName;
    private String storeAddress;
    private List<String> snsAddress;
    private String phone;
    private LocalTime openTime;
    private LocalTime closeTime;
}
