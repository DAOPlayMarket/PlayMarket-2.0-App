package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IcoAppsRepository {
    private IcoAppsRepositoryCallback callback;

    public void getIcoApps(IcoAppsRepositoryCallback callback) {
        this.callback = callback;
        RestApi.getServerApi().getIcoApps()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::onIcoAppsReady, this::onIcoAppsFailed);
    }

    private void onIcoAppsReady(ArrayList<App> apps) {
        callback.onIcoAppsReady(apps);
    }

    private void onIcoAppsFailed(Throwable throwable) {
        callback.onIcoAppsFailed(throwable);
    }

    public interface IcoAppsRepositoryCallback {
        void onIcoAppsReady(ArrayList<App> apps);

        void onIcoAppsFailed(Throwable throwable);
    }
}
