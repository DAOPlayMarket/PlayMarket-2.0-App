package com.blockchain.store.playmarket.ui.transfer_screen;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppBuyTransactionModel;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.SendEthereumTransactionModel;
import com.blockchain.store.playmarket.data.entities.SendTokenTransactionModel;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.BigInt;

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
        SendEthereumTransactionModel transactionModel = new SendEthereumTransactionModel();
        transactionModel.priceInWei = transferAmount;
        transactionModel.addressTo = recipientAddress;

        Observable<AccountInfoResponse> accountInfoResponseObservable = RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex());
        accountInfoResponseObservable
                .flatMap(accountInfoResponse -> {
                    String transaction = generateTransaction(accountInfoResponse, transferAmount, recipientAddress);
                    return RestApi.getServerApi().deployTransaction(transaction);
                })
                .map(result -> TransactionInteractor.mapWithJobService(result, transactionModel))
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
        AppBuyTransactionModel transactionModel = new AppBuyTransactionModel();
        transactionModel.boughtApp = app;
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(result -> mapAppBuyTransaction(result, app))
                .map(result -> TransactionInteractor.mapWithJobService(result, transactionModel))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {
        view.closeTransferActivity();
    }

    private void transferFailed(Throwable throwable) {
        view.showToast(throwable.getMessage());
    }

    private Observable<PurchaseAppResponse> mapAppBuyTransaction(Pair<AccountInfoResponse, String> accountInfo, App app) {

        String rawTransaction = "";
        try {
            rawTransaction = CryptoUtils.generateAppBuyTransaction(
                    accountInfo.first.count,
                    new BigInt(Long.parseLong(accountInfo.second)),
                    app, accountInfo.first.adrNode);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().deployTransaction(rawTransaction);

    }

    public void createTransferTokenTransaction(String transferAmount, String recipientAddress, String icoAddress, SendTokenTransactionModel tokenTransactionModel) {

        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(result -> mapTokenTransfer(result, transferAmount, recipientAddress, icoAddress))
                .map(result -> TransactionInteractor.mapWithJobService(result, tokenTransactionModel))
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
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().deployTransaction(rawTransaction);
    }
}
