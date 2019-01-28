package com.blockchain.store.playmarket.utilities;

import android.content.Context;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;

public class FingerprintUtils {

    public static boolean isFingerprintAvailibility(Context context) {
        return RxFingerprint.isAvailable(context) && isUserPasswordStored();
    }

    public static boolean isFingerprintAvailibility(Context context, String account) {
        HashMap<String, String> map = Hawk.get(Constants.ENCRYPTED_PASSWORD_MAP);
        if (map.containsKey(account)) {
            return RxFingerprint.isAvailable(context);
        } else {
            return false;
        }


    }

    public static boolean isUserPasswordStored() {
        String encryptedPassword = getEncryptedPassword();
        return encryptedPassword != null;
    }

    public static String getEncryptedPassword() {
        if (Hawk.contains(Constants.ENCRYPTED_PASSWORD_MAP)) {
            HashMap<String, String> map = Hawk.get(Constants.ENCRYPTED_PASSWORD_MAP);
            String key = AccountManager.getAddress().getHex();
            return map.get(key);
        } else {
            return null;
        }
    }

    public static String getPasswordForAddress(String address) {
        HashMap<String, String> map = Hawk.get(Constants.ENCRYPTED_PASSWORD_MAP, new HashMap<>());
        return map.get(address);

    }

    public static void addEncryptedPassword(String encrypted) {
        HashMap<String, String> map = Hawk.get(Constants.ENCRYPTED_PASSWORD_MAP, new HashMap<>());
        map.put(AccountManager.getAddress().getHex(), encrypted);
        Hawk.put(Constants.ENCRYPTED_PASSWORD_MAP, map);
    }
}

