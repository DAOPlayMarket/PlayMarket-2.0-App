package com.blockchain.store.playmarket.ui.navigation_view;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.data.entities.UserBalance;

import java.util.List;

/**
 * Created by Crypton04 on 09.02.2018.
 */

public class NavigationViewContract {
    public interface View {

        void onBalanceReady(UserBalance balance);

        void onBalanceFail(Throwable throwable);

        void showUserBalanceProgress(boolean isShow);

        void onLocalTokensReady(List<DaoToken> daoTokens);

        void onLocalTokensError(Throwable throwable);
    }

    interface Presenter {
        void init(View view);

        void loadUserBalance();
    }
}
