package org.wtm.web.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wtm.web.ticket.dto.TicketResponseDto;
import org.wtm.web.ticket.service.TicketService;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreTicketController {

    private final TicketService ticketService;

    @GetMapping("/{storeId}/tickets")
    public ResponseEntity<?> getTicketsByStoreId(@PathVariable Long storeId) {
        // 테스트용 userId 설정 (고정된 값 사용)
        Long userId = 1L; // 예시 userId
        TicketResponseDto response = ticketService.getTicketsByStoreId(storeId, userId);
        if (response == null) {
            return ResponseEntity.status(404).body("티켓 정보를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }
}
