package com.blockchain.store.playmarket.utilities;


import com.orhanobut.hawk.Hawk;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.KeyStore;
import org.web3j.utils.Numeric;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by Crypton04 on 25.01.2018.
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
            return keyManager.getAccounts().get(getCurrentUserPosition()).getAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Account getAccount() {
        try {
            return keyManager.getAccounts().get(getCurrentUserPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserBalance() {
        if (userBalance == null) return "-1";
        return userBalance;
    }

    public static void setUserBalance(String userBalance) {
        AccountManager.userBalance = userBalance;
    }

    public static String getFormattedAddress() {
        return NumberUtils.formatStringToSpacedNumber(getAddress().getHex());
    }

    public static int getCurrentUserPosition() {
        return Hawk.get(Constants.USER_ACCOUNT_POSITION, 0);
    }

    public static void setCurrentUserPosition(int position) {
        Hawk.put(Constants.USER_ACCOUNT_POSITION, position);
    }

    public static boolean unlockKeystore(String password) {
        try {
            keyManager.getKeystore().unlock(AccountManager.getAccount(), password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
