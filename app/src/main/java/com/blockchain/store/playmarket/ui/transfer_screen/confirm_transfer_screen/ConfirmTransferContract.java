package com.blockchain.store.playmarket.ui.transfer_screen.confirm_transfer_screen;

import android.content.Context;

import org.ethereum.geth.BigInt;

/**
 * Created by Crypton07 on 02.03.2018.
 */

public class ConfirmTransferContract {

    interface View{
        void closeTransferDialog();

        void showToast(String message);
    }

    interface Presenter{
        void init(View view, Context context);

        boolean passwordCheck(String password);

        void createTransaction(String transferAmount, String recipientAddress);
    }
}
