package com.blockchain.store.playmarket.ui.tokens_screen;

public class TokenListPresenter implements TokenListContract.Presenter {
    private TokenListContract.View view;

    @Override
    public void init(TokenListContract.View view) {
        this.view = view;
    }

    @Override
    public void getAllTokens() {

    }
}
