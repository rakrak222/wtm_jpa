package org.wtm.web.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.ticket.dto.TicketResponseDto;
import org.wtm.web.ticket.service.TicketService;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreTicketController {

    private final TicketService ticketService;

    @GetMapping("/{storeId}/tickets")
    public ResponseEntity<?> getTicketsByStoreId(@PathVariable Long storeId, @RequestHeader("x-username") String username) {
        // 테스트용 userId 설정 (고정된 값 사용)
//        Long userId = 1L; // 예시 userId

        // username 값 출력
        System.out.println("Received username: " + username);



        TicketResponseDto response = ticketService.getTicketsByStoreId(storeId, username);
        if (response == null) {
            return ResponseEntity.status(404).body("티켓 정보를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }



    
}

