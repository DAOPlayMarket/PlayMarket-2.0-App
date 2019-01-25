package com.blockchain.store.playmarket.utilities;

import android.content.Context;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.Map;

public class FingerprintUtils {

    public static boolean isFingerprintAvailibility(Context context) {
        return RxFingerprint.isAvailable(context) && isUserPasswordStored();
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

    public static void addEncryptedPassword(String encrypted) {
        HashMap<String, String> map = Hawk.get(Constants.ENCRYPTED_PASSWORD_MAP, new HashMap<>());
        map.put(AccountManager.getAddress().getHex(), encrypted);
        Hawk.put(Constants.ENCRYPTED_PASSWORD_MAP, map);
    }
}

