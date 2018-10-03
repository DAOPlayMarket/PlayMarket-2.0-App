package com.blockchain.store.PurchaseSDK.repository;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.BigInt;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransactionFactory {
    /* What needed to create transaction:
     * 1. App Id.
     * 2. Transfer amount.
     * 3. Nonce, Gas Price, Gas Limit, Node Address (this can get with 'get-user-tx' request)
     * 4.
     * */
    public static Observable<PurchaseAppResponse> get(Constants.TransactionTypes types, String transferAmount, String recipientAddress, String icoAddress, String packageName) {
        Observable<App> appByPackageName = findAppByPackageName(packageName);


        switch (types) {
            case BUY_APP:
                generateAppPurchase(transferAmount, recipientAddress, icoAddress);
            case INVEST:
                break;
            case TRANSFER:
                break;
            case TRANSFER_TOKEN:
                break;
            case SEND_REVIEW:
                break;
        }

        return null;
    }

    public static Observable<PurchaseAppResponse> generateAppPurchase(String transferAmount, String recipientAddress, String icoAddress) {
        return RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> mapTokenTransfer(result, transferAmount, recipientAddress, icoAddress))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<App> findAppByPackageName(String packageName) {
        return RestApi.getServerApi().getAppsByPackage(packageName)
                .map(result -> result.get(0))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static Observable<PurchaseAppResponse> mapTokenTransfer(AccountInfoResponse accountInfo, String transferAmount, String recipientAddress, String icoAddress) {
        String rawTransaction = "";
        try {
            rawTransaction = CryptoUtils.generateSendTokenTransaction(
                    accountInfo.count,
                    new BigInt(Long.parseLong(accountInfo.gasPrice)),
                    transferAmount,
                    recipientAddress,
                    icoAddress);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().deployTransaction(rawTransaction);
    }

}
