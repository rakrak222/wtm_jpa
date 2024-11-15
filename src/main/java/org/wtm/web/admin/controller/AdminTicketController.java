package org.wtm.web.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.admin.dto.ticket.*;
import org.wtm.web.admin.service.AdminTicketService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminTicketController {

    private final AdminTicketService adminTicketService;

    @GetMapping("/stores/{storeId}/tickets")
    public ResponseEntity<List<TicketDto>> getTickets(
            @PathVariable Long storeId
    ) {
        try {
            List<TicketDto> tickets = adminTicketService.getTicketsByStoreId(storeId);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/stores/{storeId}/tickets")
    public ResponseEntity<?> createTicket(
            @PathVariable Long storeId,
            @RequestBody TicketDto ticketDto
    ) {
        try{
            TicketResponseDto responseDto = adminTicketService.createTicket(storeId, ticketDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/stores/{storeId}/tickets/{ticketId}")
    public ResponseEntity<?> updateTicket(
            @PathVariable Long storeId,
            @PathVariable Long ticketId,
            @RequestBody TicketDto ticketDto
    ) {
        try {
            TicketResponseDto responseDto = adminTicketService.updateTicket(storeId, ticketId, ticketDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/stores/{storeId}/tickets/{ticketId}")
    public ResponseEntity<?> deleteTicket(
            @PathVariable Long storeId,
            @PathVariable Long ticketId
    ) {
        try {
            adminTicketService.deleteTicket(storeId, ticketId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 식권 사용 컨트롤러
    @PostMapping("ticket/usage")
    public ResponseEntity<?> createTicketUsage(
            @RequestBody TicketUsageDto ticketUsageDto
    ) {
        try {
           TicketHistoryResponseDto ticketHistoryResponseDto = adminTicketService.createTicketUsageHistory(ticketUsageDto);
            return new ResponseEntity<>(ticketHistoryResponseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 식권 발급 컨트롤러
    @PostMapping("ticket/purchase")
    public ResponseEntity<?> createTicketPurchase(
            @RequestBody TicketPurchaseDto ticketPurchaseDto
            ) {
        try {
            TicketHistoryResponseDto ticketHistoryResponseDto = adminTicketService.createTicketPurchaseHistory(ticketPurchaseDto);
            return new ResponseEntity<>(ticketHistoryResponseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
