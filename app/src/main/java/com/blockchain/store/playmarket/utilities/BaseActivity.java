package com.blockchain.store.playmarket.utilities;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public BaseActivity(){
        LocaleUtils.updateConfig(this);
    }
}
