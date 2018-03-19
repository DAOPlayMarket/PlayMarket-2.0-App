package com.blockchain.store.playmarket.data.content;

import android.util.Log;

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
    private static final String TAG = "AppsManager";

    public void loadNextList(ArrayList<AppDispatcherType> dispatcherTypes, AppDispatcherListeners listeners) {
        for (AppDispatcherType dispatcherType : dispatcherTypes) {
            Log.d(TAG, "loadNextList: skip count  " + dispatcherType.apps.size());
            RestApi.getServerApi().getApps(String.valueOf(dispatcherType.categoryId), dispatcherType.apps.size(), Constants.DOWNLOAD_APPS_PER_REQUEST, dispatcherType.subCategoryId, true)
                    .map(apps -> {
//                        AppDispatcherType appDispatcherType = new AppDispatcherType(dispatcherType.categoryId, dispatcherType.subCategoryId, 0);
//                        appDispatcherType.apps.addAll(apps);
//                        appDispatcherType.totalCount = apps.size();
                        dispatcherType.previousItemCount = dispatcherType.apps.size();
                        dispatcherType.apps.addAll(apps);
                        return dispatcherType;
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listeners::onNewItemCountChanged, error -> listeners.onNewItemError(dispatcherType,error));
        }
    }

    @Override
    public void onNewItemCountChanged(int count, ArrayList<App> apps) {

    }
}
