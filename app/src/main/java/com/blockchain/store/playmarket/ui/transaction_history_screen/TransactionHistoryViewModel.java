package com.blockchain.store.playmarket.ui.transaction_history_screen;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.blockchain.store.playmarket.data.entities.TransactionModel;

import java.util.ArrayList;

public class TransactionHistoryViewModel extends ViewModel {
    public MutableLiveData<ArrayList<TransactionModel>> transactionModels = new MutableLiveData<>();
}
