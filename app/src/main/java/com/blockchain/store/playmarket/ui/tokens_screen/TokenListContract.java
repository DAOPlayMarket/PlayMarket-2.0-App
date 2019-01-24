package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.dao.data.entities.DaoToken;

import java.util.ArrayList;

public class TokenListContract {

    interface View {

        void onTokensReady(ArrayList<DaoToken> tokenResponse);

        void onNewTokenFailed(Throwable throwable);

        void onNewTokenReady(DaoToken token);

        void showProgress(boolean isShown);

        void onTokensError(Throwable throwable);

        void showFindTokenProgress(boolean isShown);
    }

    interface Presenter {
        void init(View view);

        void getAllTokens();

        void findToken(String address);
    }
}
