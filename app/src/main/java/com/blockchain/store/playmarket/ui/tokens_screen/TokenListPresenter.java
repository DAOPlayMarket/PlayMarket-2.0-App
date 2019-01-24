package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.data.entities.TokenResponse;
import com.blockchain.store.playmarket.repositories.TransactionRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenListPresenter implements TokenListContract.Presenter {
    private TokenListContract.View view;

    @Override
    public void init(TokenListContract.View view) {
        this.view = view;
    }

    @Override
    public void getAllTokens() {
        new RestApi().getCustomUrlApi(Constants.TOKEN_URL)
                .getAllTokens()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showProgress(true))
                .doOnTerminate(() -> view.showProgress(false))
                .subscribe(this::onTokensReady, this::onTokensError);
    }


    private void onTokensReady(TokenResponse tokenResponse) {
        view.onTokensReady(tokenResponse.tokens);
    }

    private void onTokensError(Throwable throwable) {
        view.onTokensError(throwable);
    }

    @Override
    public void findToken(String address) {
        TransactionRepository.getDaoTokenFullInfo(address)
                .doOnSubscribe(() -> view.showFindTokenProgress(true))
                .doOnTerminate(() -> view.showFindTokenProgress(false))
                .subscribe(this::onNewTokenReady, this::onNewTokenFailed);
    }

    private void onNewTokenReady(DaoToken token) {
        view.onNewTokenReady(token);
    }

    private void onNewTokenFailed(Throwable throwable) {
        view.onNewTokenFailed(throwable);
    }

}
