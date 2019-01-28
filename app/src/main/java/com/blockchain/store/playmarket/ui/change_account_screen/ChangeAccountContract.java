package com.blockchain.store.playmarket.ui.change_account_screen;

import android.util.Pair;

import java.util.ArrayList;

public class ChangeAccountContract {
    interface View {

        void onBalanceReady(Pair<String, String> pair);
    }

    interface Presenter {
        void init(View view);
        void loadBalances();

        void updateBalance(String address);
    }
}
