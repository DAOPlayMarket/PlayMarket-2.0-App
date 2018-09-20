package com.blockchain.store.playmarket.ui.transaction_history_screen;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

public class TransactionHistoryActivityContract {
    interface View {

    }

    interface Presenter {
        void init(View view);

        ArrayList<TransactionModel> getAllTransaction();

    }
}
