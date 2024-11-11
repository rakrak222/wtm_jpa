package org.wtm.web.user.dto.bookmark;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

@Getter
@Builder
public class BookmarkSaveDto {
    private User user;
    private Store store;
}
