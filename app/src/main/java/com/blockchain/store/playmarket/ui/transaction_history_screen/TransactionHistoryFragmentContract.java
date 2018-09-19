package com.blockchain.store.playmarket.ui.transaction_history_screen;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

public class TransactionHistoryFragmentContract {
    interface View {

    }

    interface Presenter {
        void init(View view);

        ArrayList<TransactionModel> getTransactionsByStatus(Constants.TransactionStatus status);

        ArrayList<TransactionModel> getAllTransaction();

    }
}
