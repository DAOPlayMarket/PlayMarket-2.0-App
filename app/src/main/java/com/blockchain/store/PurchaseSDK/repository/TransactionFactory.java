package com.blockchain.store.PurchaseSDK.repository;

import com.blockchain.store.PurchaseSDK.services.RemoteConstants;
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

import static android.drm.DrmStore.Action.TRANSFER;

public class TransactionFactory {
    /* What needed to create transaction:
     * 1. App Id.
     * 2. Transfer amount.
     * 3. Nonce, Gas Price, Gas Limit, Node Address (this can get with 'get-user-tx' request)
     * 4.
     * */
    public static Observable<PurchaseAppResponse> get(int transactionType, String transferAmount, String recipientAddress, String icoAddress, String packageName) {
        final Observable<App> appByPackageName = findAppByPackageName(packageName);



        getMethodByTransactionType(transactionType);



        return appByPackageName.flatMap(result -> getMethodByTransactionType(transactionType)).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    private static Observable<PurchaseAppResponse> getMethodByTransactionType(int transactionType) {
        switch (transactionType) {
            case RemoteConstants.TRANSACTION_BUY:
//                createdTranscation = generateAppPurchase(transferAmount, recipientAddress, icoAddress);
//                break;
            case RemoteConstants.TRANSACTION_BUY_OBJECT:
//                return generateAppPurchase(transferAmount, recipientAddress, icoAddress);
                break;
//            case RemoteConstants.TRANSACTION_BUY_OBJECT_WITH_PRICE_CHECK:
//                break;
//            case RemoteConstants.TRANSACTION_BUY_SUB:
//                break;
//            case RemoteConstants.TRANSACTION_BUY_SUB_WITH_PRICE:
//                break;
            default:
//                createdTranscation = null;

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
            rawTransaction = CryptoUtils.generateRemoteBuyTransaction(
                    accountInfo,
                    transferAmount,
                    recipientAddress,
                    icoAddress);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().deployTransaction(rawTransaction);
    }

}
