package com.blockchain.store.dao.ui.dao_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.dao.repository.DaoTransactionRepository;
import com.blockchain.store.dao.ui.services.DaoContractService;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.DaoToken;

import java.util.ArrayList;
import java.util.List;

public class DaoActivity extends AppCompatActivity {
    private static final String TAG = "DaoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        startService(new Intent(this, DaoContractService.class));
        DaoTransactionRepository.getTokens().subscribe(this::onOk, this::onError);

    }

    private void onOk(List<DaoToken> daoTokens) {
        Log.d(TAG, "onOk: ");
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "onError() called with: throwable = [" + throwable + "]");
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, DaoContractService.class));
        super.onStop();
    }
}
