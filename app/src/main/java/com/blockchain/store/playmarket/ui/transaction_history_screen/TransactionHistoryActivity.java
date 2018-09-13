package com.blockchain.store.playmarket.ui.transaction_history_screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.TransactionPrefsUtil;

import org.web3j.crypto.TransactionUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class TransactionHistoryActivity extends AppCompatActivity {
    private static final String TAG = "TransactionHistoryActiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        ButterKnife.bind(this);
        ArrayList<TransactionModel> storedTransactionModels = TransactionPrefsUtil.getStoredTransactionModels();
        Log.d(TAG, "onCreate: ");
    }
}
