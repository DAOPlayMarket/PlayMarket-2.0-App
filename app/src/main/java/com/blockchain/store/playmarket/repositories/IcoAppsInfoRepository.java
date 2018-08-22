package com.blockchain.store.playmarket.repositories;

import android.util.Pair;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.IcoBalance;
import com.blockchain.store.playmarket.utilities.AccountManager;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IcoAppsInfoRepository {
    private IcoAppsRepositoryCallback callback;

    public void getIcoApps(IcoAppsRepositoryCallback callback) {
        this.callback = callback;
        RestApi.getServerApi().getIcoApps()
                .flatMap(this::mapWithGetIcoBalance, Pair::new)
                .map(this::mapWithCombineResult)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(() -> this.callback.onIcoAppsSubscribed())
                .doOnTerminate(() -> this.callback.onIcoAppsTerminated())
                .subscribe(callback::onIcoAppsReady, callback::onIcoAppsFailed);
    }

    public Observable<ArrayList<AppInfo>> getIcoApps() {
        return RestApi.getServerApi().getIcoApps()
                .flatMap(this::mapWithGetIcoBalance, Pair::new)
                .map(this::mapWithCombineResult)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    private Observable<ArrayList<IcoBalance>> mapWithGetIcoBalance(ArrayList<AppInfo> apps) {
        ArrayList<String> icoAddressesArr = new ArrayList<>();
        for (int i = 0; i < apps.size(); i++) {
            icoAddressesArr.add(apps.get(i).adrICO);
        }
        String icoAddressesStr = arrayToString(icoAddressesArr);
        return RestApi.getServerApi().getBalanceOf(icoAddressesStr, AccountManager.getAddress().getHex());
    }

    private ArrayList<AppInfo> mapWithCombineResult(Pair<ArrayList<AppInfo>, ArrayList<IcoBalance>> arrayOfPairs) {
        for (int i = 0; i < arrayOfPairs.first.size(); i++) {
            arrayOfPairs.first.get(i).icoBalance = arrayOfPairs.second.get(i);
        }

        return arrayOfPairs.first;
    }

    private String arrayToString(ArrayList<String> arr) {
        String result = "";
        for (int i = 0; i < arr.size(); i++) {
            if (i == 0) result = "[\"" + arr.get(i) + "\", ";
            else if (i == arr.size() - 1) result = result + "\"" + arr.get(i) + "\"]";
            else result = result + "\"" + arr.get(i) + "\", ";
        }
        return result;
    }

    public static ArrayList<AppInfo> filterWithEmptyBalanc(ArrayList<AppInfo> apps) {
        ArrayList<AppInfo> resultList = new ArrayList<>();
        for (AppInfo app : apps) {
            if (app.icoBalance != null
                    && !app.icoBalance.balanceOf.isEmpty()
                    && !app.icoBalance.balanceOf.equalsIgnoreCase("0")) {
                resultList.add(app);
            }
        }
        return resultList;
    }


    public interface IcoAppsRepositoryCallback {
        void onIcoAppsReady(ArrayList<AppInfo> apps);

        void onIcoAppsFailed(Throwable throwable);

        default void onIcoAppsSubscribed() {

        }

        default void onIcoAppsTerminated() {

        }


    }
}
