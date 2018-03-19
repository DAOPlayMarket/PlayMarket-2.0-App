package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 29.01.2018.
 */

public class AppDispatcherType {
    public int previousItemCount = 0;
    public String categoryId;
    public int subCategoryId;
    public int totalCount;
    public boolean isInitialized = false;
    public ArrayList<App> apps = new ArrayList<>();
    public boolean isContainsError = false;

    public AppDispatcherType(String categoryId, int subCategoryId, int totalCount) {
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.totalCount = totalCount;
    }

    public AppDispatcherType() {
    }
}
