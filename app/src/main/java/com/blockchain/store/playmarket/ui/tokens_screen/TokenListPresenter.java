package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.repositories.TokenRepository;

import java.util.ArrayList;

public class TokenListPresenter implements TokenListContract.Presenter {
    private TokenListContract.View view;

    @Override
    public void init(TokenListContract.View view) {
        this.view = view;
    }

    @Override
    public void getAllTokens() {
        ArrayList<Token> userTokens = new TokenRepository().getUserTokens();
        onTokensReady(userTokens);
    }

    private void onTokensReady(ArrayList<Token> tokenResponse) {
        view.onTokensReady(tokenResponse);
    }
}
