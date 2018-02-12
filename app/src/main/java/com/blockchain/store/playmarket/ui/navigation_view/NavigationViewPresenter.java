package com.blockchain.store.playmarket.ui.navigation_view;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.BalanceResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 09.02.2018.
 */

public class NavigationViewPresenter implements NavigationViewContract.Presenter {
    NavigationViewContract.View view;

    @Override
    public void init(NavigationViewContract.View view) {
        this.view = view;
    }

    @Override
    public void loadUserBalance() {
        String accountAddress = AccountManager.getAddress().toString();
        RestApi.getServerApi().getBalance(accountAddress)
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBalanceReady,this::onBalanceFail);
    }

    private void onBalanceReady(BalanceResponse balanceResponse) {
        view.onBalanceReady(balanceResponse.balance);
    }

    private void onBalanceFail(Throwable throwable) {
        view.onBalanceFail(throwable);
    }
}
