package com.blockchain.store.playmarket.ui.transfer_screen.confirm_transfer_screen;

import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConfirmTransferPresenter implements ConfirmTransferContract.Presenter {

    private ConfirmTransferContract.View view;

    @Override
    public void init(ConfirmTransferContract.View view) {
        this.view = view;
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accountInfoResponse -> generateTransaction(accountInfoResponse, transferAmount, recipientAddress));
    }

    private void generateTransaction(AccountInfoResponse accountInfoResponse, String transferAmount, String address) {
        String rawTransaction;
        try {
            rawTransaction = CryptoUtils.generateTransferTransaction(accountInfoResponse.count, accountInfoResponse.gasPrice, transferAmount, address);
            transferTheAmount(rawTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transferTheAmount(String transaction) {
        Observable<PurchaseAppResponse> transferTheAmount = RestApi.getServerApi().transferTheAmount(transaction);
        transferTheAmount
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {
        Log.d("transfer", purchaseAppResponse.hash);
    }

    private void transferFailed(Throwable throwable) {
        Log.d("transfer", throwable.getMessage());
    }
}
