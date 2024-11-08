package org.wtm.web.user.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.common.repository.*;
import org.wtm.web.common.service.UploadService;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.review.model.Review;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.Ticket;
import org.wtm.web.ticket.model.TicketHistoryPurchase;
import org.wtm.web.ticket.model.TicketHistoryUsage;
import org.wtm.web.user.dto.UserResponseDto;
import org.wtm.web.user.dto.UserUpdateDto;
import org.wtm.web.user.dto.bookmark.BookmarkDto;
import org.wtm.web.user.dto.review.UserReviewDto;
import org.wtm.web.user.dto.ticket.*;
import org.wtm.web.user.model.User;
import org.wtm.web.user.service.MyPageService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Value("${image.upload-profile-dir}")
    private String uploadDir;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getMyPage(Long id) {
        User user = userRepository.findUserById(id);

        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public UserResponseDto getMySettings(Long id) {

        User user = userRepository.findUserById(id);

        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .build();
    }

    @Transactional
    public boolean updateMySettings(UserUpdateDto userUpdateDto) {

        User existedUser = userRepository.findByEmail(userUpdateDto.getEmail()).orElse(null);

        if (existedUser != null) {
            existedUser.updateName(userUpdateDto.getName());
            existedUser.updateAddress(userUpdateDto.getAddress());
            existedUser.updatePhone(userUpdateDto.getPhone());

            // 프로필 사진 업로드 처리
            MultipartFile file = userUpdateDto.getProfilePicture();
            if (file != null) {  // 파일이 null이 아니고 비어있지 않으면
                String savedFileUrl = uploadService.uploadFile(file, uploadDir);
                existedUser.updateProfilePicture(savedFileUrl);
            }

            // 비밀번호 인코딩 처리
            String encodedPassword = passwordEncoder.encode(userUpdateDto.getPassword());
            existedUser.updatePassword(encodedPassword);
            return true;
        }
        return false;
    }

    public List<TicketSummaryDto> getTicketsOwnedByUser(Long userId) {
        // Step 1: 구입 티켓과 사용 티켓 조회
        List<TicketHistoryPurchase> purchasedTickets = ticketHistoryPurchaseRepository.findByUserId(userId);
        List<TicketHistoryUsage> usedTickets = ticketHistoryUsageRepository.findByUserId(userId);

        Map<Long, TicketSummaryDto> ticketMap = new HashMap<>();

        // (1) 구입 티켓 수량을 ticketId로 그룹화하여 Map에 저장
        purchasedTickets.forEach(purchaseTicket -> {
            Long ticketId = purchaseTicket.getTicket().getId();
            // 아래 코드는 ticketId로 된 key가 없을 시, 해당 데이터를 key로 그리고 value를 ticketSummaryDto 객체로 넣어주는 것
            TicketSummaryDto ticketSummaryDto = ticketMap.computeIfAbsent(ticketId, k ->
                    TicketSummaryDto.form(
                            purchaseTicket.getTicket(),
                            purchaseTicket.getTicket().getStore(),
                            0L,
                            0L,
                            0L,
                            0.0
                    ));
            ticketSummaryDto.addPurchasedTickets(purchaseTicket.getAmount());

        });

        // (2) 사용 티켓 수량을 ticketId로 Map에 추가
        usedTickets.forEach(usedTicket -> {
            Long ticketId = usedTicket.getTicket().getId();
            TicketSummaryDto ticketSummaryDto = ticketMap.get(ticketId);
            if (ticketSummaryDto != null) {
                ticketSummaryDto.addUsedTickets(usedTicket.getAmount());
            }
        });

        // Step 2: 남은 티켓 수가 0보다 큰 티켓을 필터링하여 storeId 수집
        List<Long> storeIds = new ArrayList<>();
        //  values.stream이라서 ticketMap의 value인 ticketsummaryDto를 stream화 한다.
        List<TicketSummaryDto> ticketListInfo = ticketMap.values().stream()
                .filter(ticketSummaryDto -> ticketSummaryDto.getTicketAmount() > 0)
                .peek(ticketSummaryDto -> storeIds.add(ticketSummaryDto.getStoreId()))
                .collect(Collectors.toList());

        // Step 3: storeId에 해당하는 리뷰의 평균 점수를 계산
        Map<Long, Double> reviewAvgMap = storeIds.stream()
                .collect(Collectors.toMap(
                        storeId -> storeId,
                        reviewRepository::calculateAvgByStoreId
                ));

        // Step 4: store 정보 가져오기 및 최종 JSON 생성
        List<Store> stores = storeRepository.findByIdIn(storeIds);
        Map<Long, Store> storeMap = stores.stream()
                .collect(Collectors.toMap(Store::getId, store -> store));
        // step 5 : bookmark 데이터 조회도 추가
        Map<Long, Boolean> bookmarkMap = storeIds.stream()
                .collect(Collectors.toMap(
                        storeId -> storeId,
                        storeId -> bookmarkRepository.findOneByStoreIdAndUserId(storeId, userId) != null
                ));

        ticketListInfo.forEach(ticketInfo -> {
            Long storeId = ticketInfo.getStoreId();

            double reviewAverage = reviewAvgMap.get(storeId);
            ticketInfo.addReviewAverage(reviewAverage);

            boolean isBookmarked = bookmarkMap.getOrDefault(storeId, false);
            ticketInfo.checkBookmark(isBookmarked);
        });

        return ticketListInfo;
    }

    @Transactional
    public boolean useMyTicket(TicketUsageDto ticketUsageDto) {
        Integer purchasedTicketAmount =
                ticketHistoryPurchaseRepository.getPurchaseAmountByUserId(ticketUsageDto.getUserId());
        if(purchasedTicketAmount != null && purchasedTicketAmount > 0){
            TicketHistoryUsage ticketHistoryUsage = TicketHistoryUsage.builder()
                    .user(userRepository.findById(ticketUsageDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")))
                    .ticket(ticketRepository.findById(ticketUsageDto.getTicketId()).orElseThrow(() -> new IllegalArgumentException("티켓 정보를 찾을 수 없습니다.")))
                    .amount(ticketUsageDto.getAmount())
                    .build();

            // 기존 엔티티가 존재하지 않을 경우에만 저장
            if (ticketHistoryUsage.getId() == null) {
                ticketHistoryUsageRepository.save(ticketHistoryUsage);
                return true;
            }else {
                System.out.println(ticketHistoryUsage.getId() + "는 이미 존재합니다.");
                return false;
            }
        }
        System.out.println("사용할 티켓이 존재하지 않습니다");
        return false;
    }

    @Transactional
    public boolean purchaseMyTicket(TicketPurchaseDto ticketPurchaseDto) {

        TicketHistoryPurchase ticketHistoryPurchase = TicketHistoryPurchase.builder()
                .user(userRepository.findById(ticketPurchaseDto.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")))
                .ticket(ticketRepository.findById(ticketPurchaseDto.getTicketId())
                        .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다.")))
                .amount(ticketPurchaseDto.getAmount())
                .build();
        ticketHistoryPurchaseRepository.save(ticketHistoryPurchase);

        return true;
    }

    public TicketHistoryResponseDto getMyTicketHistory(Long userId, int month, int year) {
        // 시작일과 종료일 설정
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // TicketHistoryUsage 데이터 가져오기
        List<TicketAllHistoryDto> usageHistory = ticketHistoryUsageRepository
                .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
                .stream()
                .map(usage -> TicketAllHistoryDto.builder()
                        .id(usage.getId())
                        .userId(userId)
                        .amount(usage.getAmount())
                        .ticketId(usage.getTicket().getId())
                        .type("usage")
                        .regDate(usage.getRegDate())
                        .price(usage.getAmount()*ticketRepository.findPriceById(usage.getTicket().getId()))
                        .storeName(usage.getTicket().getStore().getName())
                        .build())
                .collect(Collectors.toList());

        // TicketHistoryPurchase 데이터 가져오기
        List<TicketAllHistoryDto> purchaseHistory = ticketHistoryPurchaseRepository
                .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
                .stream()
                .map(purchase -> TicketAllHistoryDto.builder()
                        .id(purchase.getId())
                        .userId(userId)
                        .amount(purchase.getAmount())
                        .ticketId(purchase.getTicket().getId())
                        .type("purchase")
                        .regDate(purchase.getRegDate())
                        .price(purchase.getAmount()*ticketRepository.findPriceById(purchase.getTicket().getId()))
                        .storeName(purchase.getTicket().getStore().getName())
                        .build())
                .collect(Collectors.toList());

        // totalPurchasedPrice와 totalUsedPrice 계산
        Long totalPurchasedPrice = purchaseHistory.stream()
                .mapToLong(purchase -> purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicketId()))
                .sum();

        Long totalUsedPrice = usageHistory.stream()
                .mapToLong(usage -> usage.getAmount() * ticketRepository.findPriceById(usage.getTicketId()))
                .sum();

        // 전체 티켓 개수 계산 (purchase - usage)
        Long totalAmount = purchaseHistory.stream().mapToLong(TicketAllHistoryDto::getAmount).sum()
                - usageHistory.stream().mapToLong(TicketAllHistoryDto::getAmount).sum();

        // 두 목록을 합치고 날짜로 정렬
        // 메서드화 시킨다면, combinedHistory 로 합체 및 정렬화 시키는거 따로 뺄것.
        // 이름 달라야할 이유가 오로지 하나의 서비스여서그럼.
        List<TicketAllHistoryDto> combinedHistoryForAll = Stream.concat(purchaseHistory.stream(), usageHistory.stream())
                .sorted(Comparator.comparing(TicketAllHistoryDto::getRegDate))
                .collect(Collectors.toList());

        return TicketHistoryResponseDto.builder()
                .combinedHistory(combinedHistoryForAll)
                .totalPurchasedPrice(totalPurchasedPrice)
                .totalUsedPrice(totalUsedPrice)
                .totalAmount(totalAmount)
                .build();
    }


    public TicketHistoryResponseDto getMyTicketHistoryByStore(Long userId, Long storeId, int month, int year) {
        // 시작일과 종료일 설정
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // storeId로 티켓 목록 가져오기
        List<Ticket> tickets = ticketRepository.findByStoreId(storeId);
        List<Long> ticketIds = tickets.stream()
                .map(Ticket::getId)
                .collect(Collectors.toList());

        // TicketHistoryUsage 데이터 가져오기 (ticketId 조건 추가)
        List<TicketAllHistoryDto> usageHistory = ticketHistoryUsageRepository
                .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
                .stream()
                .filter(usage -> ticketIds.contains(usage.getTicket().getId()))
                .map(usage -> {
                    String storeName = usage.getTicket().getStore().getName();
                    System.out.println(storeName);
                    return TicketAllHistoryDto.builder()
                            .id(usage.getId())
                            .userId(userId)
                            .amount(usage.getAmount())
                            .ticketId(usage.getTicket().getId())
                            .price(usage.getAmount()*ticketRepository.findPriceById(usage.getTicket().getId()))
                            .type("usage")
                            .regDate(usage.getRegDate())
                            .storeName(storeName)
                            .build();
                })
                .collect(Collectors.toList());

        // TicketHistoryPurchase 데이터 가져오기 (ticketId 조건 추가)
        List<TicketAllHistoryDto> purchaseHistory = ticketHistoryPurchaseRepository
                .findByUserIdAndRegDateBetween(userId, startDateTime, endDateTime)
                .stream()
                .filter(purchase -> ticketIds.contains(purchase.getTicket().getId()))
                .map(purchase -> {
                    String storeName = purchase.getTicket().getStore().getName();
                    System.out.println(storeName);
                    return TicketAllHistoryDto.builder()
                            .id(purchase.getId())
                            .userId(userId)
                            .amount(purchase.getAmount())
                            .ticketId(purchase.getTicket().getId())
                            .price(purchase.getAmount()*ticketRepository.findPriceById(purchase.getTicket().getId()))
                            .type("purchase")
                            .regDate(purchase.getRegDate())
                            .storeName(storeName)
                            .build();
                })
                .collect(Collectors.toList());

        // 두 목록을 합치고 날짜로 정렬
        List<TicketAllHistoryDto> combinedHistory = Stream.concat(purchaseHistory.stream(), usageHistory.stream())
                .sorted(Comparator.comparing(TicketAllHistoryDto::getRegDate))
                .collect(Collectors.toList());

        // totalPurchasedPrice와 totalUsedPrice 계산
        Long totalPurchasedPrice = purchaseHistory.stream()
                .mapToLong(purchase -> purchase.getAmount() * ticketRepository.findPriceById(purchase.getTicketId()))
                .sum();

        Long totalUsedPrice = usageHistory.stream()
                .mapToLong(usage -> usage.getAmount() * ticketRepository.findPriceById(usage.getTicketId()))
                .sum();

        // 전체 티켓 개수 계산 (purchase - usage)
        Long totalAmount = purchaseHistory.stream().mapToLong(TicketAllHistoryDto::getAmount).sum()
                - usageHistory.stream().mapToLong(TicketAllHistoryDto::getAmount).sum();

        // 두 목록을 합치고 날짜로 정렬
        List<TicketAllHistoryDto> combinedHistoryForStore = Stream.concat(purchaseHistory.stream(), usageHistory.stream())
                .sorted(Comparator.comparing(TicketAllHistoryDto::getRegDate))
                .collect(Collectors.toList());

        return TicketHistoryResponseDto.builder()
                .combinedHistory(combinedHistoryForStore)
                .totalPurchasedPrice(totalPurchasedPrice)
                .totalUsedPrice(totalUsedPrice)
                .totalAmount(totalAmount)
                .build();
    }


    public List<UserReviewDto> getMyReviews(Long userId) {
        //userId로 review와 해당하는 가게 정보 가져옴
        List<Review> reviews = reviewRepository.findByUserId(userId);
        if(!reviews.isEmpty()){
            // 각 리뷰에 대해 평균 점수와 북마크 상태를 가져옴
            List<UserReviewDto> reviewsWithAverageScore = reviews.stream().map(review -> {
                // @Query를 이용해 평균 점수를 한 번에 가져옴
                Double averageScore = reviewScoreRepository.findAverageScoreByReviewId(review.getId());
                // 리뷰와 평균 점수를 합쳐서 반환
                return UserReviewDto.builder()
                        .reviewId(review.getId())
                        .content(review.getContent())
                        .regDate(review.getRegDate())
                        .averageScore(averageScore != null ? averageScore : 0.0)
                        .storeName(review.getStore().getName()) // 필요한 Store 필드를 추가
//                         .storeImageUrl()
                        .build();
            }).collect(Collectors.toList());

            return reviewsWithAverageScore;
        }
        return null;
    }

    public List<BookmarkDto> getMyBookmarks(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);

        if(!bookmarks.isEmpty()) {
            // Bookmark 데이터를 BookmarkDto로 변환하여 리스트로 반환
            return bookmarks.stream()
                    .map(bookmark -> {
                        Long storeId = bookmark.getStore().getId();
                        // storeId로 티켓 정보 조회
                        Ticket ticket = ticketRepository.findOneByStoreId(storeId);

                        return BookmarkDto.builder()
                                .storeId(storeId)
                                .storeName(bookmark.getStore().getName())
                                .storeOpenTime(bookmark.getStore().getOpenTime())
                                .storeCloseTime(bookmark.getStore().getCloseTime())
                                .ticketId(ticket != null ? ticket.getId() : null)
                                .ticketPrice(ticket != null ? ticket.getPrice() : 0)
                                .reviewAverage(reviewRepository.calculateAvgByStoreId(storeId)) // @Query를 이용한 평균 점수 계산
                                .isBookmarked(true)
                                .build();
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    public boolean deleteMyReview(Long reviewId) {
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));
        reviewRepository.deleteById(reviewId);
        return true;
    }

    @Transactional
    public boolean saveMyBookmark(Long storeId, Long userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Bookmark bookmark = bookmarkRepository.findByStoreIdAndUserId(storeId, userId)
                .orElseGet(() -> Bookmark.builder()
                        .store(store)
                        .user(user)
                        .build());

        if (bookmark.getId() == null) {
            bookmarkRepository.save(bookmark);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteMyBookmark(Long storeId, Long userId) {
        Bookmark bookmark = bookmarkRepository.findByStoreIdAndUserId(storeId, userId)
                .orElse(null);

        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
            return true;
        }
        return false;
    }


}
