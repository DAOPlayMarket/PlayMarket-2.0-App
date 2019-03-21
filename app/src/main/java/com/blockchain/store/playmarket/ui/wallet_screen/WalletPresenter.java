package com.blockchain.store.playmarket.ui.wallet_screen;

import android.util.Log;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.repository.ContractReader;
import com.blockchain.store.dao.repository.DaoTransactionRepository;
import com.blockchain.store.playmarket.repositories.TokenRepository;
import com.blockchain.store.playmarket.repositories.UserBalanceRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class WalletPresenter implements WalletContract.Presenter {

    private static final String TAG = "NavigationViewPresenter";

    private WalletContract.View view;

    @Override
    public void init(WalletContract.View view) {
        this.view = view;
    }

    @Override
    public void loadUserBalance() {
        String accountAddress = AccountManager.getAddress().getHex();
        Log.d(TAG, "loadUserBalance: account address " + accountAddress);
        new UserBalanceRepository().getUserBalance(accountAddress)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showUserBalanceProgress(true))
                .doOnTerminate(() -> view.showUserBalanceProgress(false))
                .subscribe(view::onBalanceReady, view::onBalanceFail);
    }

    @Override
    public void getWalletTokens() {

        ArrayList<DaoToken> tokens = TokenRepository.getUserSavedTokens();
        tokens.add(0, new DaoToken().generatePmToken());
        view.onLocalTokensAdded(tokens);

        PublishSubject<DaoToken> publishSubject = PublishSubject.create();
        publishSubject.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(view::onTokenNext, view::onTokenError, view::onTokensComplete);
        ContractReader.getWalletTokens(publishSubject, tokens);


        DaoTransactionRepository.getOnlyPmtToken()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onOk,this::onFailed);


    }

    private void onOk(List<DaoToken> daoTokens) {
        Log.d(TAG, "onOk: ");
    }

    private void onFailed(Throwable throwable) {
        Log.d(TAG, "onFailed: ");
    }

}
