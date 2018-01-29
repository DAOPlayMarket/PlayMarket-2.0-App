package com.blockchain.store.playmarket.data.content;

import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.entities.SubCategory;
import com.blockchain.store.playmarket.data.listeners.AppDispatcherListeners;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class AppsDispatcher {
    private static final String TAG = "AppsDispatcher";
    private static ArrayList<AppDispatcherType> providers = new ArrayList<>();
    private static ArrayList<AppDispatcherListeners> listeners = new ArrayList<>();

    public void loadNewData(ArrayList<AppDispatcherType> dispatcherTypes, AppDispatcherListeners listeners) {
        Application.getAppsManager().loadNextList(dispatcherTypes, listeners);
    }

    public void loadNewData(AppDispatcherType dispatcherType, AppDispatcherListeners listeners) {
        ArrayList<AppDispatcherType> appDispatcherTypes = new ArrayList<>();
        appDispatcherTypes.add(dispatcherType);
        Application.getAppsManager().loadNextList(appDispatcherTypes, listeners);
    }


    public void addListener(AppDispatcherListeners listener) {
        listeners.add(listener);

    }

    public void removeListener(AppDispatcherListeners listener) {
        listeners.remove(listener);
    }

    public ArrayList<AppDispatcherType> addProviders(Category category) {
        ArrayList<AppDispatcherType> returnList = new ArrayList<>();
        for (SubCategory subCategory : category.subCategories) {
            AppDispatcherType appDispatcherType = new AppDispatcherType(String.valueOf(category.id), subCategory.id, 0);
            if (!isAlreadyHasProvider(appDispatcherType)) {
                providers.add(appDispatcherType);
            }
            returnList.add(appDispatcherType);
        }
        Log.d(TAG, "addProviders. Total providers: " + providers.toString());
        return returnList;
    }

    public void removeProvider(Category category) {
        removeFromProvider(category);
    }

    private boolean isAlreadyHasProvider(AppDispatcherType appDispatcherType) {
        if (providers.isEmpty()) {
            return false;
        } else {
            for (AppDispatcherType provider : providers) {
                if (provider.categoryId.equalsIgnoreCase(appDispatcherType.categoryId) && provider.subCategoryId == appDispatcherType.subCategoryId) {
                    return true;
                }
            }
            return false;
        }
    }

    private void removeFromProvider(Category category) {
        if (providers.isEmpty()) return;
        for (SubCategory subCategory : category.subCategories) {
            for (Iterator<AppDispatcherType> it = providers.iterator(); it.hasNext(); ) {
                AppDispatcherType provider = it.next();
                if (provider.categoryId.equalsIgnoreCase(String.valueOf(category.id)) && provider.subCategoryId == subCategory.id) {
                    it.remove();
                }
            }
        }

        Log.d(TAG, "removeFromProvider: NewList." + providers.toString());
    }

}
