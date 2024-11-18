package org.wtm.web.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.dashboard.DashboardDto;
import org.wtm.web.admin.dto.info.StoreInfoDto;
import org.wtm.web.admin.dto.info.StoreInfoUpdateDto;
import org.wtm.web.admin.dto.notice.NoticeListDto;
import org.wtm.web.admin.service.AdminStoreService;

import java.util.List;


@RestController
@RequestMapping("api/v1/admin")
@AllArgsConstructor
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getDashboard(@PathVariable("storeId") Long storeId) {
        try {
            DashboardDto dashboardDto = adminStoreService.getDashboardByStoreId(storeId);
            return new ResponseEntity<>(dashboardDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/stores/{storeId}/info")
    public ResponseEntity<?> getInfo(@PathVariable("storeId") Long storeId) {
        try {
            StoreInfoDto storeInfoDto = adminStoreService.getStoreInfoByStoreId(storeId);
            return new ResponseEntity<>(storeInfoDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/stores/{storeId}/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateInfo(
            @PathVariable("storeId") Long storeId,
            @RequestParam("dto") String rawDto,
            @RequestParam(value = "img", required = false) MultipartFile img
    ) {
        try {
            System.out.println("Raw DTO: " + rawDto);// JSON 데이터 디버깅
            System.out.println("Img: " + img);
            // JSON 문자열을 DTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            StoreInfoUpdateDto dto = objectMapper.readValue(rawDto, StoreInfoUpdateDto.class);

            adminStoreService.updateStoreInfoByStoreId(storeId, dto, img);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}

// 데이터
//ALTER TABLE `USER` ADD COLUMN profilePicture VARCHAR(255);
//ALTER TABLE STORE ADD COLUMN address VARCHAR(255);
//
//ALTER TABLE REVIEW_COMMENT ADD CONSTRAINT fk_review_comment_review FOREIGN KEY (review_id) REFERENCES REVIEW (review_id);
//
//INSERT INTO REVIEW_SCALE (review_scale_id, name) VALUES (1, '맛');
//INSERT INTO REVIEW_SCALE (review_scale_id, name) VALUES (2, '청결도');
//INSERT INTO REVIEW_SCALE (review_scale_id, name) VALUES (3, '분위기');
//INSERT INTO REVIEW_SCALE (review_scale_id, name) VALUES (4, '친절도');
//
//INSERT INTO `USER` (user_id, email, password, name, phone, role, profilePicture) VALUES (1, 'admin@store.com', 'password', 'Admin User', '123-456-7890', 'ADMIN', 'admin_profile.jpg');
//INSERT INTO `USER` (user_id, email, password, name, phone, role, profilePicture) VALUES (2, 'user@store.com', 'password', 'Regular User', '098-765-4321', 'USER', 'user_profile.jpg');
//
//INSERT INTO STORE (store_id, name, address, contact) VALUES (1, 'Store One', '123 Main St', '111-222-3333');
//
//INSERT INTO REVIEW (review_id, store_id, user_id, content, revisit, regDate) VALUES (1, 1, 2, 'Great store, had a wonderful experience!', 1, '2024-10-01 10:00:00');
//INSERT INTO REVIEW (review_id, store_id, user_id, content, revisit, regDate) VALUES (2, 1, 2, 'Not so good this time.', 0, '2024-10-02 11:30:00');
//
//INSERT INTO REVIEW_COMMENT (review_comment_id, user_id, store_id, review_id, content) VALUES (1, 1, 1, 1, 'Thank you for your kind words');
//INSERT INTO REVIEW_COMMENT (review_comment_id, user_id, store_id, review_id, content) VALUES (2, 1, 1, 2, 'We apologize for the inconvenience');
//
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (1, 1, 1, 4.5);
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (2, 1, 2, 4.0);
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (3, 1, 3, 3.5);
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (4, 1, 4, 5.0);
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (5, 2, 1, 2.0);
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (6, 2, 2, 1.5);
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (7, 2, 3, 2.5);
//INSERT INTO REVIEW_SCORE (review_score_id, review_id, review_scale_id, score) VALUES (8, 2, 4, 3.0);
//
//INSERT INTO NOTICE (notice_id, store_id, user_id, title, content, regDate) VALUES
//(1, 1, 1, 'Holiday Hours', 'We will be closed on national holidays.', '2024-10-01 09:00:00'),
//        (2, 1, 1, 'New Menu Items', 'Check out our new dishes available now!', '2024-10-15 09:30:00');
