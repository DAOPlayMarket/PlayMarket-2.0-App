package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.data.entities.TokenResponse;

import java.util.ArrayList;

public class TokenListContract {

    interface View {

        void onTokensReady(ArrayList<Token> tokenResponse);

        void onNewTokenFailed(Throwable throwable);

        void onNewTokenReady(Token token);

        void showProgress(boolean isShown);

        void onTokenBalanceReady(Token token);

        void onBottomSheetTokensFailed(Throwable throwable);

        void onBottomSheetTokensReady(ArrayList<Token> tokens);
    }

    interface Presenter {
        void init(View view);

        void getAllTokens();

        void findToken(String address);
    }
}
