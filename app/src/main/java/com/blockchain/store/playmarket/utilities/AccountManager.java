package com.blockchain.store.playmarket.utilities;


import com.blockchain.store.playmarket.Application;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by Igor.Sakovich on 25.01.2018.
 */

public class AccountManager {
    private static KeyManager keyManager;
    private static AccountManager instance;
    private static String userBalance;

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
    /*
    * Generate Signed Transaction
    *  Transaction tx = new Transaction(0,AccountManager.getAddress(),new BigInt(0),new BigInt(21000),new BigInt(1000002359L),null);
     try {
     String s = tx.encodeJSON();
     Log.d(TAG, "unsigned transaction: " +s);
     } catch (Exception e) {
     e.printStackTrace();
     }
     try {
     Account account = Application.keyManager.getAccounts().get(0);
     KeyStore keystore = Application.keyManager.getKeystore();
     Transaction signedTranscation = keystore.signTxPassphrase(account, "", tx, new BigInt(4));
     String s = signedTranscation.encodeJSON();
     Log.d(TAG, "signed transaction: " + s);
     } catch (Exception e) {
     e.printStackTrace();
     }
    * */
}
