package com.blockchain.store.playmarket;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.orhanobut.hawk.Hawk;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class Application extends MultiDexApplication {
    public static KeyManager keyManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ToastUtil.setContext(this);
        keyManager = KeyManager.newKeyManager(getFilesDir().getAbsolutePath());
        AccountManager.setKeyManager(keyManager);
        Hawk.init(this);

    }
}
