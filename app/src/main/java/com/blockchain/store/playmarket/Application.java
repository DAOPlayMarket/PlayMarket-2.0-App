package com.blockchain.store.playmarket;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blockchain.store.playmarket.utilities.ToastUtil;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class Application extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ToastUtil.setContext(this);
    }
}
