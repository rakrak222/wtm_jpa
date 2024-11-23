package org.wtm.web.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.auth.dto.Address;
import org.wtm.web.user.dto.review.ReviewListDto;
import org.wtm.web.user.dto.review.ReviewPageResponse;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.common.repository.*;
import org.wtm.web.common.service.UploadService;
import org.wtm.web.review.model.Review;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.Ticket;
import org.wtm.web.ticket.model.TicketHistoryPurchase;
import org.wtm.web.ticket.model.TicketHistoryUsage;
import org.wtm.web.user.dto.user.UserResponseDto;
import org.wtm.web.user.dto.user.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.ticket.*;
import org.wtm.web.user.mapper.UserReviewMapper;
import org.wtm.web.user.model.User;
import org.wtm.web.user.service.MyPageService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMyPageService implements MyPageService {

    private final UploadService uploadService;

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final TicketRepository ticketRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TicketHistoryPurchaseRepository ticketHistoryPurchaseRepository;
    private final TicketHistoryUsageRepository ticketHistoryUsageRepository;
    private final ReviewScoreRepository reviewScoreRepository;
    private final ReviewImgRepository reviewImgRepository;

    private final UserReviewMapper userReviewMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getMyPage(String username) {
        User user = userRepository.findOneByEmail(username);

        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public UserResponseDto getMySettings(String username) {
        User user = userRepository.findOneByEmail(username);

        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .userAddress(user.getAddress())
                .phone(user.getPhone())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    @Transactional
    public boolean updateMySettings(UserUpdateDto dto, MultipartFile profilePicture) {

        User existedUser = userRepository.findByEmail(dto.getEmail()).orElse(null);

        Address address = dto.getUserAddress();
        System.out.println(dto.getUserAddress().getAddress());
        if (existedUser != null) {
            existedUser.updateName(dto.getName());
            existedUser.updateAddress(address);
            existedUser.updatePhone(dto.getPhone());

            // 프로필 사진 업로드 처리
            MultipartFile file = profilePicture;
            if (file != null && !file.isEmpty()) {  // 파일이 null이 아니고 비어있지 않으면
                String savedFileUrl = uploadService.uploadFile(file, "users");
                if (savedFileUrl != null) {
                    existedUser.updateProfilePicture(savedFileUrl);
                }
            }

            // 비밀번호 인코딩 처리
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            existedUser.updatePassword(encodedPassword);
            return true;
        }
        return false;
    }

    public List<TicketSummaryDto> getTicketsOwnedByUser(String username) {
        Long userId = userRepository.findIdByEmail(username);

        // Step 1: 구입 티켓과 사용 티켓 조회
        List<TicketHistoryPurchase> purchasedTickets = ticketHistoryPurchaseRepository.findByUserId(userId);
        List<TicketHistoryUsage> usedTickets = ticketHistoryUsageRepository.findByUserId(userId);

        // Step 2: TicketSummaryDto를 storeId 기준으로 그룹화
        Map<Long, TicketSummaryDto> storeSummaryMap = new HashMap<>();

        // 구입 티켓을 처리
        purchasedTickets.forEach(purchaseTicket -> {
            Ticket ticket = purchaseTicket.getTicket();
            Store store = ticket.getStore();
            Long storeId = store.getId();

            TicketSummaryDto summary = storeSummaryMap.computeIfAbsent(storeId, k -> TicketSummaryDto.form(
                    ticket,
                    store,
                    0L,
                    0L,
                    0L,
                    0.0
            ));

            summary.addPurchasedTickets(purchaseTicket.getAmount());
        });

        // 사용 티켓을 처리
        usedTickets.forEach(usedTicket -> {
            Ticket ticket = usedTicket.getTicket();
            Long storeId = ticket.getStore().getId();

            TicketSummaryDto summary = storeSummaryMap.get(storeId);
            if (summary != null) {
                summary.addUsedTickets(usedTicket.getAmount());
            }
        });

        // Step 3: storeId별 리뷰 평균 점수와 북마크 상태를 추가
        List<Long> storeIds = new ArrayList<>(storeSummaryMap.keySet());

        Map<Long, Double> reviewAvgMap = storeIds.stream()
                .collect(Collectors.toMap(
                        storeId -> storeId,
                        storeId -> {
                            Double avg = reviewRepository.calculateAvgByStoreId(storeId);
                            return avg != null ? Math.round(avg * 10) / 10.0 : 0.0;
                        }
                ));

        Map<Long, Boolean> bookmarkMap = storeIds.stream()
                .collect(Collectors.toMap(
                        storeId -> storeId,
                        storeId -> bookmarkRepository.existsByStoreIdAndUserId(storeId, userId)
                ));

        storeSummaryMap.values().forEach(summary -> {
            Long storeId = summary.getStoreId();
            summary.addReviewAverage(reviewAvgMap.getOrDefault(storeId, 0.0));
            summary.checkBookmark(bookmarkMap.getOrDefault(storeId, false));
        });

        // Step 4: 최종 리스트 반환
        return new ArrayList<>(storeSummaryMap.values());
    }

    public TicketHistoryResponseDto getMyTicketHistory(String username, int month, int year, String type, Pageable pageable) {
        Long userId = userRepository.findIdByEmail(username);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // 초기화
        List<TicketAllHistoryDto> combinedHistory;
        Long totalPurchasedPrice = 0L;
        Long totalUsedPrice = 0L;
        Long totalAmount = 0L;
        int totalPages = 0;
        long totalItems = 0;

        // type에 따라 로직 분기
        if ("usage".equalsIgnoreCase(type)) {
            Page<TicketHistoryUsage> usageHistoryPage = ticketHistoryUsageRepository
                    .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime, pageable);

            combinedHistory = usageHistoryPage.stream()
                    .map(usage -> TicketAllHistoryDto.builder()
                            .storeId(usage.getTicket().getStore().getId())
                            .id(usage.getId())
                            .userId(userId)
                            .amount(usage.getAmount())
                            .ticketId(usage.getTicket().getId())
                            .type("usage")
                            .regDate(usage.getRegDate())
                            .price(usage.getAmount() * ticketRepository.findPriceById(usage.getTicket().getId()))
                            .storeName(usage.getTicket().getStore().getName())
                            .hasReview(usage.getReview() != null)
                            .build())
                    .collect(Collectors.toList());

            totalPages = usageHistoryPage.getTotalPages();
            totalItems = usageHistoryPage.getTotalElements();

        } else if ("purchase".equalsIgnoreCase(type)) {
            Page<TicketHistoryPurchase> purchaseHistoryPage = ticketHistoryPurchaseRepository
                    .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime, pageable);

            combinedHistory = purchaseHistoryPage.stream()
                    .map(purchase -> TicketAllHistoryDto.builder()
                            .storeId(purchase.getTicket().getStore().getId())
                            .id(purchase.getId())
                            .userId(userId)
                            .amount(purchase.getAmount())
                            .ticketId(purchase.getTicket().getId())
                            .type("purchase")
                            .regDate(purchase.getRegDate())
                            .price(purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicket().getId()))
                            .storeName(purchase.getTicket().getStore().getName())
                            .build())
                    .collect(Collectors.toList());

            totalPages = purchaseHistoryPage.getTotalPages();
            totalItems = purchaseHistoryPage.getTotalElements();

        } else { // 기본값: all
            Page<TicketHistoryUsage> usageHistoryPage = ticketHistoryUsageRepository
                    .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime, pageable);

            Page<TicketHistoryPurchase> purchaseHistoryPage = ticketHistoryPurchaseRepository
                    .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime, pageable);

            List<TicketAllHistoryDto> usageHistory = usageHistoryPage.stream()
                    .map(usage -> TicketAllHistoryDto.builder()
                            .storeId(usage.getTicket().getStore().getId())
                            .id(usage.getId())
                            .userId(userId)
                            .amount(usage.getAmount())
                            .ticketId(usage.getTicket().getId())
                            .type("usage")
                            .regDate(usage.getRegDate())
                            .price(usage.getAmount() * ticketRepository.findPriceById(usage.getTicket().getId()))
                            .storeName(usage.getTicket().getStore().getName())
                            .hasReview(usage.getReview() != null)
                            .build())
                    .collect(Collectors.toList());

            List<TicketAllHistoryDto> purchaseHistory = purchaseHistoryPage.stream()
                    .map(purchase -> TicketAllHistoryDto.builder()
                            .storeId(purchase.getTicket().getStore().getId())
                            .id(purchase.getId())
                            .userId(userId)
                            .amount(purchase.getAmount())
                            .ticketId(purchase.getTicket().getId())
                            .type("purchase")
                            .regDate(purchase.getRegDate())
                            .price(purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicket().getId()))
                            .storeName(purchase.getTicket().getStore().getName())
                            .build())
                    .collect(Collectors.toList());

            combinedHistory = Stream.concat(purchaseHistory.stream(), usageHistory.stream())
                    .sorted(Comparator.comparing(TicketAllHistoryDto::getRegDate)) // ASC 정렬
                    .collect(Collectors.toList());

            totalPages = Math.max(usageHistoryPage.getTotalPages(), purchaseHistoryPage.getTotalPages());
            totalItems = usageHistoryPage.getTotalElements() + purchaseHistoryPage.getTotalElements();
        }

        // totalPurchasedPrice와 totalUsedPrice 계산 (페이지네이션과 무관하게 전체 데이터 기준)
        totalPurchasedPrice = ticketHistoryPurchaseRepository.findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
                .stream()
                .mapToLong(
                        purchase -> purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicket().getId())
                )
                .sum();

        totalUsedPrice = ticketHistoryUsageRepository.findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
                .stream()
                .mapToLong(usage -> usage.getAmount() * ticketRepository.findPriceById(usage.getTicket().getId()))
                .sum();
        totalAmount = ticketHistoryPurchaseRepository
                        .getPurchaseAmountByUserId(userId).orElse(0L)
                    - ticketHistoryUsageRepository
                        .getUsageAmountByUserId(userId).orElse(0L);

        int currentPage = pageable.getPageNumber() + 1; // 0부터 시작하므로 +1

        return TicketHistoryResponseDto.builder()
                .combinedHistory(combinedHistory)
                .totalPurchasedPrice(totalPurchasedPrice)
                .totalUsedPrice(totalUsedPrice)
                .totalAmount(totalAmount)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();
    }

    public TicketHistoryResponseDto getMyTicketHistoryByStore(String username, Long storeId, int month, int year, String type, Pageable pageable) {
        Long userId = userRepository.findIdByEmail(username);
        // 시작일과 종료일 설정
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<TicketAllHistoryDto> combinedHistory;
        Long totalPurchasedPrice = 0L;
        Long totalUsedPrice = 0L;
        Long totalAmount = 0L;
        int totalPages = 0;
        long totalItems = 0;

        // storeId로 티켓 목록 가져오기
        List<Ticket> tickets = ticketRepository.findByStoreId(storeId);
        List<Long> ticketIds = tickets.stream()
                .map(Ticket::getId)
                .collect(Collectors.toList());

        System.out.println("tickets :" + tickets);

        if ("usage".equalsIgnoreCase(type)) {
            Page<TicketHistoryUsage> usageHistoryPage = ticketHistoryUsageRepository
                    .findByUserIdAndRegDateBetweenAndTicketIdIn(userId, startDateTime, endDateTime, ticketIds, pageable);

            combinedHistory = usageHistoryPage.stream()
                    .map(usage -> TicketAllHistoryDto.builder()
                            .id(usage.getId())
                            .userId(userId)
                            .amount(usage.getAmount())
                            .ticketId(usage.getTicket().getId())
                            .price(usage.getAmount() * ticketRepository.findPriceById(usage.getTicket().getId()))
                            .type("usage")
                            .regDate(usage.getRegDate())
                            .storeName(usage.getTicket().getStore().getName())
                            .hasReview(usage.getReview() != null)
                            .build())
                    .collect(Collectors.toList());

            totalPages = usageHistoryPage.getTotalPages();
            totalItems = usageHistoryPage.getTotalElements();

        } else if ("purchase".equalsIgnoreCase(type)) {
            Page<TicketHistoryPurchase> purchaseHistoryPage = ticketHistoryPurchaseRepository
                    .findByUserIdAndRegDateBetweenAndTicketIdIn(userId, startDateTime, endDateTime, ticketIds, pageable);

            combinedHistory = purchaseHistoryPage.stream()
                    .map(purchase -> TicketAllHistoryDto.builder()
                            .id(purchase.getId())
                            .userId(userId)
                            .amount(purchase.getAmount())
                            .ticketId(purchase.getTicket().getId())
                            .price(purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicket().getId()))
                            .type("purchase")
                            .regDate(purchase.getRegDate())
                            .storeName(purchase.getTicket().getStore().getName())
                            .build())
                    .collect(Collectors.toList());

            totalPages = purchaseHistoryPage.getTotalPages();
            totalItems = purchaseHistoryPage.getTotalElements();

        } else { // 기본값: all
            Page<TicketHistoryUsage> usageHistoryPage = ticketHistoryUsageRepository
                    .findByUserIdAndRegDateBetweenAndTicketIdIn(userId, startDateTime, endDateTime, ticketIds, pageable);

            Page<TicketHistoryPurchase> purchaseHistoryPage = ticketHistoryPurchaseRepository
                    .findByUserIdAndRegDateBetweenAndTicketIdIn(userId, startDateTime, endDateTime, ticketIds, pageable);

            List<TicketAllHistoryDto> usageHistory = usageHistoryPage.stream()
                    .map(usage -> TicketAllHistoryDto.builder()
                            .id(usage.getId())
                            .userId(userId)
                            .amount(usage.getAmount())
                            .ticketId(usage.getTicket().getId())
                            .price(usage.getAmount() * ticketRepository.findPriceById(usage.getTicket().getId()))
                            .type("usage")
                            .regDate(usage.getRegDate())
                            .storeName(usage.getTicket().getStore().getName())
                            .hasReview(usage.getReview() != null)
                            .build())
                    .collect(Collectors.toList());

            List<TicketAllHistoryDto> purchaseHistory = purchaseHistoryPage.stream()
                    .map(purchase -> TicketAllHistoryDto.builder()
                            .id(purchase.getId())
                            .userId(userId)
                            .amount(purchase.getAmount())
                            .ticketId(purchase.getTicket().getId())
                            .price(purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicket().getId()))
                            .type("purchase")
                            .regDate(purchase.getRegDate())
                            .storeName(purchase.getTicket().getStore().getName())
                            .build())
                    .collect(Collectors.toList());

            combinedHistory = Stream.concat(purchaseHistory.stream(), usageHistory.stream())
                    .sorted(Comparator.comparing(TicketAllHistoryDto::getRegDate))
                    .collect(Collectors.toList());

            totalPages = Math.max(usageHistoryPage.getTotalPages(), purchaseHistoryPage.getTotalPages());
            totalItems = usageHistoryPage.getTotalElements() + purchaseHistoryPage.getTotalElements();
        }

        // totalPurchasedPrice와 totalUsedPrice 계산
        totalPurchasedPrice = ticketHistoryPurchaseRepository.findByUserIdAndRegDateBetweenAndTicketIdIn(userId, startDateTime, endDateTime, ticketIds)
                .stream()
                .mapToLong(purchase -> purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicket().getId()))
                .sum();

        totalUsedPrice = ticketHistoryUsageRepository.findByUserIdAndRegDateBetweenAndTicketIdIn(userId, startDateTime, endDateTime, ticketIds)
                .stream()
                .mapToLong(usage -> usage.getAmount() * ticketRepository.findPriceById(usage.getTicket().getId()))
                .sum();

        totalAmount = ticketHistoryPurchaseRepository
                .getPurchaseAmountByUserIdAndTicketIdIn(userId, ticketIds).orElse(0L)
                - ticketHistoryUsageRepository
                .getUsageAmountByUserIdAndTicketIdIn(userId, ticketIds).orElse(0L);

        int currentPage = pageable.getPageNumber() + 1; // 0부터 시작하므로 +1

        return TicketHistoryResponseDto.builder()
                .combinedHistory(combinedHistory)
                .totalPurchasedPrice(totalPurchasedPrice)
                .totalUsedPrice(totalUsedPrice)
                .totalAmount(totalAmount)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();
    }

//    public TicketHistoryResponseDto getMyTicketHistoryByStore(String username, Long storeId, int month, int year, String type, Pageable pageable) {
//        Long userId = userRepository.findIdByEmail(username);
//        // 시작일과 종료일 설정
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
//
//        List<TicketAllHistoryDto> combinedHistory;
//        Long totalPurchasedPrice = 0L;
//        Long totalUsedPrice = 0L;
//        Long totalAmount = 0L;
//        int totalPages = 0;
//        long totalItems = 0;
//
//        // storeId로 티켓 목록 가져오기
//        List<Ticket> tickets = ticketRepository.findByStoreId(storeId);
//        List<Long> ticketIds = tickets.stream()
//                .map(Ticket::getId)
//                .collect(Collectors.toList());
//
//        // TicketHistoryUsage 데이터 가져오기 (ticketId 조건 추가)
//        List<TicketAllHistoryDto> usageHistory = ticketHistoryUsageRepository
//                .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
//                .stream()
//                .filter(usage -> ticketIds.contains(usage.getTicket().getId()))
//                .map(usage -> {
//                    String storeName = usage.getTicket().getStore().getName();
//                    System.out.println(storeName);
//                    return TicketAllHistoryDto.builder()
//                            .id(usage.getId())
//                            .userId(userId)
//                            .amount(usage.getAmount())
//                            .ticketId(usage.getTicket().getId())
//                            .price(usage.getAmount()*ticketRepository.findPriceById(usage.getTicket().getId()))
//                            .type("usage")
//                            .regDate(usage.getRegDate())
//                            .storeName(storeName)
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//        // TicketHistoryPurchase 데이터 가져오기 (ticketId 조건 추가)
//        List<TicketAllHistoryDto> purchaseHistory = ticketHistoryPurchaseRepository
//                .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
//                .stream()
//                .filter(purchase -> ticketIds.contains(purchase.getTicket().getId()))
//                .map(purchase -> {
//                    String storeName = purchase.getTicket().getStore().getName();
//                    System.out.println(storeName);
//                    return TicketAllHistoryDto.builder()
//                            .id(purchase.getId())
//                            .userId(userId)
//                            .amount(purchase.getAmount())
//                            .ticketId(purchase.getTicket().getId())
//                            .price(purchase.getAmount()*ticketRepository.findPriceById(purchase.getTicket().getId()))
//                            .type("purchase")
//                            .regDate(purchase.getRegDate())
//                            .storeName(storeName)
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//        // 두 목록을 합치고 날짜로 정렬
//        List<TicketAllHistoryDto> combinedHistory = Stream.concat(purchaseHistory.stream(), usageHistory.stream())
//                .sorted(Comparator.comparing(TicketAllHistoryDto::getRegDate))
//                .collect(Collectors.toList());
//
//        // totalPurchasedPrice와 totalUsedPrice 계산
//        Long totalPurchasedPrice = purchaseHistory.stream()
//                .mapToLong(purchase -> purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicketId()))
//                .sum();
//
//        Long totalUsedPrice = usageHistory.stream()
//                .mapToLong(usage -> usage.getAmount() * ticketRepository.findPriceById(usage.getTicketId()))
//                .sum();
//
//        // 전체 티켓 개수 계산 (purchase - usage)
//        Long totalAmount = purchaseHistory.stream().mapToLong(TicketAllHistoryDto::getAmount).sum()
//                - usageHistory.stream().mapToLong(TicketAllHistoryDto::getAmount).sum();
//
//        // 두 목록을 합치고 날짜로 정렬
//        List<TicketAllHistoryDto> combinedHistoryForStore = Stream.concat(purchaseHistory.stream(), usageHistory.stream())
//                .sorted(Comparator.comparing(TicketAllHistoryDto::getRegDate))
//                .collect(Collectors.toList());
//
//        return TicketHistoryResponseDto.builder()
//                .combinedHistory(combinedHistoryForStore)
//                .totalPurchasedPrice(totalPurchasedPrice)
//                .totalUsedPrice(totalUsedPrice)
//                .totalAmount(totalAmount)
//                .build();
//    }


    public ReviewPageResponse getMyReviews(Long userId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAllByUserId(userId, pageable);
        List<ReviewListDto> reviewList = reviewPage.stream()
                .map(userReviewMapper::toReviewListDto)
                .collect(Collectors.toList());
        return ReviewPageResponse.builder()
                .reviews(reviewList)
                .currentPage(reviewPage.getNumber())
                .totalPages(reviewPage.getTotalPages())
                .totalItems(reviewPage.getTotalElements())
                .build();
    }

    public List<BookmarkDto> getMyBookmarks(String username) {
        Long userId = userRepository.findIdByEmail(username);
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);

        if(!bookmarks.isEmpty()) {
            // Bookmark 데이터를 BookmarkDto로 변환하여 리스트로 반환
            return bookmarks.stream()
                    .map(bookmark -> {
                        Long storeId = bookmark.getStore().getId();
                        // storeId로 티켓 정보 조회
                        List<Ticket> tickets = ticketRepository.findByStoreId(storeId, Sort.by(Sort.Direction.DESC, "price"))
                                .orElse(null);
                        Ticket ticket;
                        Long ticketPrice = null;
                        if(!tickets.isEmpty()){
                            ticket = tickets.getFirst();

                            if(ticket.getPrice()!=null){
                                ticketPrice = ticket.getPrice();
                            }
                        }
                        String openTime = null;
                        String closeTime = null;
                        Double reviewAvgScore = null;
                        if(bookmark.getStore().getOpenTime()!=null && bookmark.getStore().getCloseTime()!=null){
                            openTime = bookmark.getStore().getOpenTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                            closeTime = bookmark.getStore().getCloseTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                        }
                        if(reviewRepository.calculateAvgByStoreId(storeId)!=null){
                            reviewAvgScore = reviewRepository.calculateAvgByStoreId(storeId);
                            if(reviewAvgScore != null){
                                reviewAvgScore = Math.round(reviewAvgScore * 10) / 10.0;
                            }
                        }
                        String storeImgUrl=null;
                        if (bookmark.getStore().getUser().getProfilePicture() != null){
                            storeImgUrl = bookmark.getStore().getUser().getProfilePicture();
                        }
                        return BookmarkDto.builder()
                                .storeId(storeId)
                                .storeName(bookmark.getStore().getName())
                                .storeOpenTime(openTime)
                                .storeCloseTime(closeTime)
                                .storeImgUrl(storeImgUrl)
                                .ticketPrice(ticketPrice)
                                .reviewAverage(reviewAvgScore) // @Query를 이용한 평균 점수 계산
                                .isBookmarked(true)
                                .build();
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    public boolean deleteMyReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElse(null);
        if(review!=null){
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean saveMyBookmark(BookmarkDto bookmarkDto) {
        String username = bookmarkDto.getUsername();
        Long storeId = bookmarkDto.getStoreId();

        if(username!=null && storeId !=null){
            Long userId = userRepository.findIdByEmail(username);
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
            // 이미 존재하는지 확인
            if (bookmarkRepository.existsByStoreIdAndUserId(storeId, userId)) {
                return false;
            }

            Bookmark bookmark = Bookmark.builder()
                    .store(store)
                    .user(user)
                    .build();

            bookmarkRepository.save(bookmark);
            return true;
        }
        System.out.println("유저이름이랑 스토어 못찾겠음");
        return false;
    }

    @Transactional
    public boolean deleteMyBookmark(Long storeId, String username) {
        Long userId = userRepository.findIdByEmail(username);

        Bookmark bookmark = bookmarkRepository.findByStoreIdAndUserId(storeId, userId)
                .orElse(null);

        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
            return true;
        }
        return false;
    }

    public TicketDto getMyTicketDetail(Long storeId, String username) {
        Long userId = userRepository.findIdByEmail(username);
        // storeId로 관련된 모든 ticket 조회 (가격 내림차순 정렬)
        List<Ticket> tickets = ticketRepository.findByStoreId(storeId, Sort.by(Sort.Direction.DESC, "price")).orElse(null);
        if (tickets.isEmpty()) {
            throw new IllegalArgumentException("No tickets found for the given storeId.");
        }
        // ticket IDs 추출
        List<Long> ticketIds = tickets.stream()
                .map(Ticket::getId)
                .toList();

        // 구매 및 사용 기록 계산
        Long purchaseAmount = ticketHistoryPurchaseRepository.countByTicketIdsAndUserId(ticketIds, userId);
        Long usageAmount = ticketHistoryUsageRepository.countByTicketIdsAndUserId(ticketIds, userId);

        long totalAmount = (purchaseAmount != null ? purchaseAmount : 0L) - (usageAmount != null ? usageAmount : 0L);

        // Bookmark 여부 확인
        Bookmark bookmark = bookmarkRepository.findOneByStoreIdAndUserId(storeId, userId);
        boolean isBookmarked = bookmark != null;

        // 첫 번째 ticket의 store 정보 사용
        Store store = tickets.get(0).getStore();

        // DTO 생성 및 반환
        return TicketDto.builder()
                .userId(userId)
                .storeName(store.getName())
                .storeId(store.getId())
                .isBookmarked(isBookmarked)
                .ticketAmount(totalAmount)
                .build();
    }

}
