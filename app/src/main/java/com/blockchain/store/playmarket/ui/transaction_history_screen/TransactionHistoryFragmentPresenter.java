package com.blockchain.store.playmarket.ui.transaction_history_screen;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.TransactionPrefsUtil;

import java.util.ArrayList;

import static com.blockchain.store.playmarket.ui.transaction_history_screen.TransactionHistoryFragmentContract.*;

public class TransactionHistoryFragmentPresenter implements Presenter {

    private View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public ArrayList<TransactionModel> getTransactionsByStatus(Constants.TransactionStatus status) {
        return TransactionPrefsUtil.getTransactionByStatus(status);
    }

    @Override
    public ArrayList<TransactionModel> getAllTransaction() {
        return TransactionPrefsUtil.getStoredTransactionModels();
    }
}
