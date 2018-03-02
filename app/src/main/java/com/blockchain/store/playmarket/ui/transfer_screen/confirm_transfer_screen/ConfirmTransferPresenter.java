package com.blockchain.store.playmarket.ui.transfer_screen.confirm_transfer_screen;

/**
 * Created by Crypton07 on 02.03.2018.
 */

public class ConfirmTransferPresenter implements ConfirmTransferContract.Presenter {

    private ConfirmTransferContract.View view;

    @Override
    public void init(ConfirmTransferContract.View view) {
        this.view = view;
    }
}
