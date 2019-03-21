package com.blockchain.store.playmarket.ui.wallet_screen;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.data.entities.UserBalance;

import java.util.ArrayList;

public class WalletContract {

    interface View {

        void onBalanceReady(UserBalance balance);

        void onBalanceFail(Throwable throwable);

        void showUserBalanceProgress(boolean isShow);

        void onTokenNext(DaoToken daoToken);

        void onTokensComplete();

        void onTokenError(Throwable throwable);

        void onLocalTokensAdded(ArrayList<DaoToken> tokens);
    }

    interface Presenter {

        void init(View view);

        void loadUserBalance();

        void getWalletTokens();

    }

}
