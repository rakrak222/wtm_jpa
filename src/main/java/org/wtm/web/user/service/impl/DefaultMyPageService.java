package org.wtm.web.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.common.repository.*;
import org.wtm.web.user.dto.UserResponseDto;
import org.wtm.web.user.dto.UserUpdateDto;
import org.wtm.web.user.model.User;
import org.wtm.web.user.service.MyPageService;

@Service
@RequiredArgsConstructor
public class DefaultMyPageService implements MyPageService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TicketHistoryPurchaseRepository ticketHistoryPurchaseRepository;
    private final TicketHistoryUsageRepository ticketHistoryUsageRepository;

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
    public void updateMySettings(UserUpdateDto userUpdateDto) {

        User existedUser = userRepository.findByEmail(userUpdateDto.getEmail()).orElse(null);

        if (existedUser != null) {
            existedUser.updateName(userUpdateDto.getName());
            existedUser.updateAddress(userUpdateDto.getAddress());
            existedUser.updatePhone(userUpdateDto.getPhone());
            existedUser.updateProfilePicture(userUpdateDto.getProfilePicture());

            // 비밀번호 인코딩 처리
            String encodedPassword = passwordEncoder.encode(userUpdateDto.getPassword());
            existedUser.updatePassword(encodedPassword);
        }
    }
}
