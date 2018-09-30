package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.playmarket.data.entities.TokenResponse;

public class TokenListContract {

    interface View {

        void showProgress(boolean b);

        void onTokensReady(TokenResponse tokenResponse);

        void onTokensError(Throwable throwable);
    }

    interface Presenter {
        void init(View view);

        void getAllTokens();
    }
}
