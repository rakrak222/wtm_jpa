package org.wtm.web.bookmark.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.bookmark.service.BookmarkService;
import org.wtm.web.common.repository.BookmarkRepository;
import org.wtm.web.common.repository.StoreRepository;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

@Service
@RequiredArgsConstructor
public class DefaultBookmarkService implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    private static final Long FIXED_USER_ID = 1L;

    @Override
    public void addBookmark(Long storeId) {
        User user = userRepository.findById(FIXED_USER_ID)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        if (bookmarkRepository.findByUserAndStore(user, store).isPresent()) {
            throw new IllegalStateException("이미 북마크된 가게입니다.");
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .store(store)
                .build();

        bookmarkRepository.save(bookmark);

    }

    @Override
    public void removeBookmark(Long storeId) {
        User user = userRepository.findById(FIXED_USER_ID)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        Bookmark bookmark = bookmarkRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new IllegalStateException("북마크가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);
    }
}
