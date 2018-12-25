package com.blockchain.store.playmarket.repositories;

import android.util.Pair;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.IcoBalance;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IcoAppsInfoRepository {
    private IcoAppsRepositoryCallback callback;

    public void getIcoApps(IcoAppsRepositoryCallback callback) {
        this.callback = callback;
        RestApi.getServerApi().getIcoApps()
//                .zipWith(RestApi.getServerApi().getAppsByPackageGetAsAppInfo("com.blockchain.cryptoduel"), Pair::new)
//                .map(this::addCryptoDuelToArray)
//                .map(this::mapWithLocalIco)
//                .flatMap(this::mapWithGetIcoBalance, Pair::new)
//                .map(this::mapWithCombineResult)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(() -> this.callback.onIcoAppsSubscribed())
                .doOnTerminate(() -> this.callback.onIcoAppsTerminated())
                .subscribe(callback::onIcoAppsReady, callback::onIcoAppsFailed);
    }

    private ArrayList<AppInfo> addCryptoDuelToArray(Pair<ArrayList<AppInfo>, ArrayList<AppInfo>> arrayListArrayListPair) {
        ArrayList<AppInfo> first = arrayListArrayListPair.first;
        ArrayList<AppInfo> second = arrayListArrayListPair.second;
        if (!second.isEmpty())
            first.add(0, second.get(0));
        return first;
    }


    public Observable<ArrayList<AppInfo>> getIcoApps() {
        return RestApi.getServerApi().getIcoApps()
                .map(this::mapWithLocalIco)
                .flatMap(this::mapWithGetIcoBalance, Pair::new)
                .map(this::mapWithCombineResult)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    private ArrayList<AppInfo> mapWithLocalIco(ArrayList<AppInfo> appInfos) {
        AppInfo localAppInfo = new AppInfo();
        localAppInfo.adrICO = Constants.PLAY_MARKET_ADDRESS;
        appInfos.add(0, localAppInfo);

        return appInfos;
    }

    private Observable<List<IcoBalance>> mapWithGetIcoBalance(ArrayList<AppInfo> apps) {
        ArrayList<Observable<IcoBalance>> userTokenListObs = new ArrayList<>();
        for (int i = 0; i < apps.size(); i++) {
            userTokenListObs.add(TransactionRepository.getIcoBalance(apps.get(i).adrICO, AccountManager.getAddress().getHex()));
        }

        return Observable.from(userTokenListObs).flatMap(result -> result.observeOn(Schedulers.computation())).toList();
    }

    private ArrayList<AppInfo> mapWithCombineResult(Pair<ArrayList<AppInfo>, List<IcoBalance>> arrayOfPairs) {
        for (int i = 0; i < arrayOfPairs.first.size(); i++) {
            for (IcoBalance icoBalance : arrayOfPairs.second) {
                if (arrayOfPairs.first.get(i).adrICO.equalsIgnoreCase(icoBalance.address)) {
                    arrayOfPairs.first.get(i).icoBalance = icoBalance;
                }
            }
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

    public static ArrayList<AppInfo> filterWithEmptyBalance(ArrayList<AppInfo> apps) {
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
