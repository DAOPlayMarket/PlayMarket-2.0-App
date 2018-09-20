package com.blockchain.store.playmarket.ui.transaction_history_screen;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.TransactionPrefsUtil;

import java.util.ArrayList;

import static com.blockchain.store.playmarket.ui.transaction_history_screen.TransactionHistoryActivityContract.*;

public class TransactionHistoryActivityPresenter implements Presenter {

    private View view;

    @Override
    public void init(View view) {
        this.view = view;
    }


    @Override
    public ArrayList<TransactionModel> getAllTransaction() {
        return TransactionPrefsUtil.getStoredTransactionModels();
    }
}
