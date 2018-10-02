package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.TokenResponse;

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
        RestApi.getServerApi().getAllTokens()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showProgress(true))
                .doOnTerminate(() -> view.showProgress(false))
                .subscribe(this::onTokensReady, this::onTokensError);
    }

    private void onTokensReady(TokenResponse tokenResponse) {
        view.onTokensReady(tokenResponse);
    }

    private void onTokensError(Throwable throwable) {
        view.onTokensError(throwable);
    }
}