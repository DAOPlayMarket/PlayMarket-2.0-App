package com.blockchain.store.playmarket.ui.transfer_screen;

import android.content.Context;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Account;

import io.ethmobile.ethdroid.KeyManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransferPresenter implements TransferContract.Presenter {

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


    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {
        Log.d("transfer", purchaseAppResponse.hash);
        view.closeTransferActivity();
        view.showToast(context.getResources().getString(R.string.transaction_success));
    }

    private void transferFailed(Throwable throwable) {
        Log.d("transfer", throwable.getMessage());
        view.showToast(throwable.getMessage());
    }
}
