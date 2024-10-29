package org.wtm.web.user.service;

import org.springframework.stereotype.Service;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.review.model.Review;
import org.wtm.web.ticket.model.Ticket;
import org.wtm.web.user.dto.UserResponseDto;
import org.wtm.web.user.dto.UserUpdateDto;

import java.util.List;

@Service
public interface MyPageService {

    public UserResponseDto getMyPage(Long id);

    public UserResponseDto getMySettings(Long id);

    public void updateMySettings(UserUpdateDto userUpdateDto);

}
