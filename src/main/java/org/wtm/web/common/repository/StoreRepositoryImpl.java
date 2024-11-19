package org.wtm.web.common.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.wtm.web.menu.model.QMeal;
import org.wtm.web.menu.model.QMenu;
import org.wtm.web.store.model.QStore;
import org.wtm.web.store.model.Store;


import java.time.LocalDate;
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

    @Override
    public List<Store> searchStoresByNameOrTodayMenu(String query) {
        QStore store = QStore.store;
        QMeal meal = QMeal.meal;
        QMenu menu = QMenu.menu;

        LocalDate today = LocalDate.now();

        return queryFactory.selectDistinct(store)
                .from(store)
                .leftJoin(store.meals, meal).on(meal.mealDate.eq(today))
                .leftJoin(meal.menus, menu)
                .where(
                        store.name.containsIgnoreCase(query)
                                .or(menu.name.containsIgnoreCase(query))
                )
                .fetch();
    }
}
