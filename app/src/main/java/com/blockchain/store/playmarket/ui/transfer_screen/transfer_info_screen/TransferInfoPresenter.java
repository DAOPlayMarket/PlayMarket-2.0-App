package com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen;

import android.content.Context;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.UserBalance;
import com.blockchain.store.playmarket.repositories.UserBalanceRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Crypton07 on 02.03.2018.
 */

public class TransferInfoPresenter implements TransferInfoContract.Presenter {

    private TransferInfoContract.View view;

    @Override
    public void init(TransferInfoContract.View view, Context context) {
        this.view = view;
    }

    @Override
    public void getAccountBalance() {
        String accountAddress = AccountManager.getAddress().getHex();
        new UserBalanceRepository().getUserBalance(accountAddress)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(() -> view.showUserBalanceProgress(true))
//                .doOnTerminate(() -> view.showUserBalanceProgress(false))
                .subscribe(this::getAccountInfoSuccessful, this::getAccountInfoBalance);
    }

    private void getAccountInfoSuccessful(UserBalance userBalance) {
        view.getAccountBalanceSuccessful(userBalance);
    }


    @Override
    public String getSenderAddress() {
        return AccountManager.getAddress().getHex();
    }

    private void getAccountInfoBalance(Throwable throwable) {
        view.onAccountBalanceError(throwable);
    }
}
