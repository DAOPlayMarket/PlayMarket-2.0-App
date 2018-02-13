package com.blockchain.store.playmarket.ui.navigation_view;

import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.BalanceResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 09.02.2018.
 */

public class NavigationViewPresenter implements NavigationViewContract.Presenter {
    private static final String TAG = "NavigationViewPresenter";

    NavigationViewContract.View view;

    @Override
    public void init(NavigationViewContract.View view) {
        this.view = view;
    }

    @Override
    public void loadUserBalance() {
        String accountAddress = AccountManager.getAddress().getHex();
        Log.d(TAG, "loadUserBalance: account address " + accountAddress);
        RestApi.getServerApi().getBalance(accountAddress)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBalanceReady, this::onBalanceFail);
    }

    private void onBalanceReady(BalanceResponse balanceResponse) {
        view.onBalanceReady(balanceResponse.balance);
    }

    private void onBalanceFail(Throwable throwable) {
        view.onBalanceFail(throwable);
    }
}
