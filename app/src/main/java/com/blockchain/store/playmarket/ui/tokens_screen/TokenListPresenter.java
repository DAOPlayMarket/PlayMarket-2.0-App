package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.data.entities.TokenResponse;
import com.blockchain.store.playmarket.repositories.TokenRepository;
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
        ArrayList<Token> userTokens = new TokenRepository().getUserTokens();
        onTokensReady(userTokens);
    }

    private void onTokensReady(ArrayList<Token> tokenResponse) {
        view.onTokensReady(tokenResponse);
    }
}
