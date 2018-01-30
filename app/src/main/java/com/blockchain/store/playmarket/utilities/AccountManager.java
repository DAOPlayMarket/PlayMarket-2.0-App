package com.blockchain.store.playmarket.utilities;


import com.blockchain.store.playmarket.Application;

import org.ethereum.geth.Address;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by Igor.Sakovich on 25.01.2018.
 */

public class AccountManager {
    private static KeyManager keyManager;
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

    public static boolean isHasUsers() {
        try {
            return !keyManager.getAccounts().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setKeyManager(KeyManager keyManager) {
        AccountManager.keyManager = keyManager;
    }

    public static Address getAddress() {
        try {
            return keyManager.getAccounts().get(0).getAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
