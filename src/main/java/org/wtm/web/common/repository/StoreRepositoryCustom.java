package org.wtm.web.common.repository;

import org.wtm.web.store.model.Store;

import java.util.List;

public interface StoreRepositoryCustom {
    List<Store> searchStores(String query);

    List<Store> searchStoresByNameOrTodayMenu(String query);
}
