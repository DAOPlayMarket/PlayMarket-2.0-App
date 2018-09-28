package com.blockchain.store.playmarket.ui.tokens_screen;

public class TokenListContract {

    interface View {

    }

    interface Presenter {
        void init(View view);

        void getAllTokens();
    }
}
