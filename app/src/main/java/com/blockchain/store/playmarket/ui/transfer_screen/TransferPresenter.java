package com.blockchain.store.playmarket.ui.transfer_screen;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Account;
import org.ethereum.geth.BigInt;

import io.ethmobile.ethdroid.KeyManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransferPresenter implements TransferContract.Presenter {
    private static final String TAG = "TransferPresenter";
    private TransferContract.View view;
    private Context context;

    @Override
    public void init(TransferContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public boolean passwordCheck(String password) {
        try {
            Application.keyManager.getKeystore().unlock(AccountManager.getAccount(), password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void createTransaction(String transferAmount, String recipientAddress) {
        Observable<AccountInfoResponse> accountInfoResponseObservable = RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex());
        accountInfoResponseObservable
                .flatMap(accountInfoResponse -> {
                    String transaction = generateTransaction(accountInfoResponse, transferAmount, recipientAddress);
                    return RestApi.getServerApi().transferTheAmount(transaction);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private String generateTransaction(AccountInfoResponse accountInfoResponse, String transferAmount, String address) {
        String rawTransaction;
        try {
            rawTransaction = CryptoUtils.generateTransferTransaction(accountInfoResponse.count, accountInfoResponse.gasPrice, transferAmount, address);
            return rawTransaction;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void createBuyTransaction(App app) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(result -> mapAppBuyTransaction(result, app))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {
        Log.d("transfer", purchaseAppResponse.hash);
        view.closeTransferActivity();
    }

    private void transferFailed(Throwable throwable) {
        Log.d("transfer", throwable.getMessage());
        view.showToast(throwable.getMessage());
    }

    private Observable<PurchaseAppResponse> mapAppBuyTransaction(Pair<AccountInfoResponse, String> accountInfo, App app) {

        String rawTransaction = "";
        try {
            rawTransaction = CryptoUtils.generateAppBuyTransaction(
                    accountInfo.first.count,
                    new BigInt(Long.parseLong(accountInfo.second)),
                    app, accountInfo.first.adrNode);
            Log.d(TAG, "handleAccountInfoResult: " + rawTransaction);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().purchaseApp(rawTransaction);

    }

    public void createTransferTokenTransaction(String transferAmount, String recipientAddress, String icoAddress) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(result -> mapTokenTransfer(result, transferAmount, recipientAddress, icoAddress))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);

    }

    private Observable<PurchaseAppResponse> mapTokenTransfer(Pair<AccountInfoResponse, String> accountInfo, String transferAmount, String recipientAddress, String icoAddress) {
        String rawTransaction = "";
        try {
            rawTransaction = CryptoUtils.generateSendTokenTransaction(
                    accountInfo.first.count,
                    new BigInt(Long.parseLong(accountInfo.second)),
                    transferAmount,
                    recipientAddress,
                    icoAddress);
            Log.d(TAG, "handleAccountInfoResult: " + rawTransaction);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().deployTransaction(rawTransaction, null);
    }
}
