package com.blockchain.store.playmarket.data.content;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.listeners.AppDataListeners;
import com.blockchain.store.playmarket.data.listeners.AppDispatcherListeners;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 29.01.2018.
 */

public class AppsManager implements AppDataListeners {
    public void loadNextList(ArrayList<AppDispatcherType> dispatcherTypes, AppDispatcherListeners listeners) {
        for (AppDispatcherType dispatcherType : dispatcherTypes) {
            RestApi.instance().getApps(String.valueOf(dispatcherType.categoryId), 0, Constants.DOWNLOAD_APPS_PER_REQUEST, dispatcherType.subCategoryId, true)
                    .map(apps -> {
                        dispatcherType.apps = apps;
                        return dispatcherType;
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listeners::onNewItemCountChanged, listeners::onNewItemError);
        }
    }

    @Override
    public void onNewItemCountChanged(int count, ArrayList<App> apps) {

    }
}
