package com.blockchain.store.playmarket.utilities;


import com.blockchain.store.playmarket.Application;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by Igor.Sakovich on 25.01.2018.
 */

public class AccountManager {
    private KeyManager keyManager;
    private static AccountManager instance;

    private AccountManager() {
        keyManager = Application.keyManager;
    }

    public static AccountManager instance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

            /* try {
            if (keyManager.getAccounts().size() > 0)
                LoadNewUserWelcomeActivity(null);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
}
