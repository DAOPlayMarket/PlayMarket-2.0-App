package com.blockchain.store.playmarket.ui.token_transfer_screen;

public class TokenTransferContract {
    interface View{

        void transferSuccess(String response);

        void transferFailed(Throwable error);

        void setProgressVisibility(boolean isVisible);
    }
    interface Presenter{
        void init(View view);
    }
}
