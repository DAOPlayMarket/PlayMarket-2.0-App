package com.blockchain.store.dao.ui.dao_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blockchain.store.dao.ui.services.DaoContractService;
import com.blockchain.store.playmarket.R;

public class DaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        startService(new Intent(this, DaoContractService.class));
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, DaoContractService.class));
        super.onStop();
    }
}
