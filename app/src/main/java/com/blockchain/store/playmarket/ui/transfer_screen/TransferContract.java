package com.blockchain.store.playmarket.ui.transfer_screen;

import android.content.Context;

/**
 * Created by Crypton07 on 15.03.2018.
 */

public class TransferContract {

    interface View{

        void closeTransferActivity();

        void showToast(String message);

    }

    interface Presenter{

        void init(TransferContract.View view, Context context);

        boolean passwordCheck(String password);

        void createTransaction(String transferAmount, String recipientAddress);

    }
}
