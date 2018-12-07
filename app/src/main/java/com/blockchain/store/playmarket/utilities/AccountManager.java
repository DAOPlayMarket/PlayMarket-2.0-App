package com.blockchain.store.playmarket.utilities;


import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.KeyStore;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by Igor.Sakovich on 25.01.2018.
 */

public class AccountManager {
    private static final String TAG = "AccountManager";

    private static KeyManager keyManager;
    private static String userBalance;

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

    public static KeyManager getKeyManager() {
        return keyManager;
    }

    public static KeyStore getKeyStore() {
        return getKeyManager().getKeystore();
    }

    public static Address getAddress() {
        try {
            return keyManager.getAccounts().get(0).getAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Account getAccount() {
        try {
            return keyManager.getAccounts().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserBalance() {
        if (userBalance == null) return "0";
        return userBalance;
    }

    public static void setUserBalance(String userBalance) {
        AccountManager.userBalance = userBalance;
    }

    public static String getFormattedAddress() {
        return NumberUtils.formatStringToSpacedNumber(getAddress().getHex());
    }

    private int getCurrentUserPosition() {
        return 0;
    }

    private void setCurrentUserPosition(int position) {
        //set
    }

}
