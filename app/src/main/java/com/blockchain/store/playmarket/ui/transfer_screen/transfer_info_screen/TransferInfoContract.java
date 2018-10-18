package com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen;

import android.content.Context;

import com.blockchain.store.playmarket.data.entities.UserBalance;

/**
 * Created by Crypton07 on 02.03.2018.
 */

public class TransferInfoContract {

    interface View{

        void getAccountBalanceSuccessful(UserBalance accountBalance);

        void onAccountBalanceError(Throwable throwable);
    }

    interface Presenter{

        void init(View view, Context context);

        void getAccountBalance();

        String getSenderAddress();

    }
}
