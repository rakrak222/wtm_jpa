package org.wtm.web.common.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wtm.web.store.model.QStore;
import org.wtm.web.store.model.Store;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Store> searchStores(String query) {
        QStore store = QStore.store;

        return queryFactory.selectFrom(store)
                .where(store.name.containsIgnoreCase(query))
                .fetch();
    }
}
