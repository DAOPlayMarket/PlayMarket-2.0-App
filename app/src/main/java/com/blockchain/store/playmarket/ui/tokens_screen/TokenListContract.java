package com.blockchain.store.playmarket.ui.tokens_screen;

import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.data.entities.TokenResponse;

import java.util.ArrayList;

public class TokenListContract {

    interface View {

        void onTokensReady(ArrayList<Token> tokenResponse);
    }

    interface Presenter {
        void init(View view);

        void getAllTokens();
    }
}
