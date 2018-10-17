package com.blockchain.store.PurchaseSDK.repository;

import com.blockchain.store.PurchaseSDK.entities.TransferObject;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.WRONG_PASSWORD_ERROR;

public class PlayMarketSdkTransactionFactory {

    public static Observable<PurchaseAppResponse> get(TransferObject transferObject) {
        String packageName = transferObject.getPackageName();
        final Observable<App> appByPackageName = findAppByPackageName(packageName);
        return appByPackageName.flatMap(result -> getMethodByTransactionType(transferObject, result)).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    private static Observable<PurchaseAppResponse> getMethodByTransactionType(TransferObject transferObject, App app) {
        transferObject.setAppId(app.getId());
        return RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> {
                    try {
                        return generateMapTransaction(result, transferObject);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(WRONG_PASSWORD_ERROR);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static Observable<PurchaseAppResponse> generateMapTransaction(AccountInfoResponse accountInfo, TransferObject transferObject) throws Exception {
        String rawTransaction = CryptoUtils.generateRemoteBuyTransaction(
                accountInfo,
                transferObject);
        return RestApi.getServerApi().deployTransaction(rawTransaction);
    }


    public static Observable<App> findAppByPackageName(String packageName) {
        return RestApi.getServerApi().getAppsByPackage(packageName)
                .map(result -> result.get(0))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
