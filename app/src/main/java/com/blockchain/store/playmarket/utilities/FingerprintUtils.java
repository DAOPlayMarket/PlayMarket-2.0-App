package com.blockchain.store.playmarket.utilities;

import android.content.Context;

import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

public class FingerprintUtils {

    public static boolean isFingerprintAvailibility(Context context) {
        return RxFingerprint.isAvailable(context) && Hawk.contains(Constants.ENCRYPTED_PASSWORD);
    }
}
