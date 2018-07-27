package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.IcoBalance;
import com.blockchain.store.playmarket.utilities.AccountManager;

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
                .doOnSubscribe(()->this.callback.onIcoAppsSubscribed())
                .doOnTerminate(()->this.callback.onIcoAppsTerminated())
                .subscribe(this::onIcoAppsReady, this::onIcoAppsFailed);
    }

    private void onIcoAppsReady(ArrayList<App> apps) {
        ArrayList<String> icoAddressesArr = new ArrayList<>();
        for (int i = 0; i<apps.size(); i++) { icoAddressesArr.add(apps.get(i).adrICO); }
        String icoAddressesStr = arrayToString(icoAddressesArr);
        RestApi.getServerApi().getBalanceOf(icoAddressesStr, AccountManager.getAddress().getHex())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ArrayList<IcoBalance> result) -> {
                    for (int i = 0; i<apps.size(); i++){ apps.get(i).icoBalance = result.get(i); }
                    callback.onIcoAppsReady(apps);
                },
                error -> callback.onIcoAppsFailed(error));
    }

    private String arrayToString(ArrayList<String> arr){
        String result = "";
        for (int i = 0; i < arr.size(); i++){
            if (i == 0) result = "[\"" + arr.get(i) + "\", ";
            else if (i == arr.size() - 1) result = result + "\"" + arr.get(i) + "\"]";
            else result = result + "\"" + arr.get(i) + "\", ";
        }
        return result;
    }

    private void onIcoAppsFailed(Throwable throwable) {
        callback.onIcoAppsFailed(throwable);
    }

    public interface IcoAppsRepositoryCallback {
        void onIcoAppsReady(ArrayList<App> apps);

        void onIcoAppsFailed(Throwable throwable);

        default void onIcoAppsSubscribed() {

        }

        default void onIcoAppsTerminated() {

        }


    }
}
