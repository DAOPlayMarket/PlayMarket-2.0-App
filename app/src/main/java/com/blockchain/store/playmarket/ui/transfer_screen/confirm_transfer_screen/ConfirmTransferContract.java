package com.blockchain.store.playmarket.ui.transfer_screen.confirm_transfer_screen;

import org.ethereum.geth.BigInt;

/**
 * Created by Crypton07 on 02.03.2018.
 */

public class ConfirmTransferContract {

    interface View{


    }

    interface Presenter{
        void init(View view);

        boolean passwordCheck(String password);

        void createTransaction(String transferAmount, String recipientAddress);
    }
}
