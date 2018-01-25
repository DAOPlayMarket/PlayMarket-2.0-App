package com.blockchain.store.playmarket.utilities.data;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import org.ethereum.geth.KeyStore;

/**
 * Created by samsheff on 21/09/2017.
 */

public class ClipboardUtils {

    public static void copyToClipboard(Context context, String data) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("data", data);
        clipboard.setPrimaryClip(clip);
    }

    public static void importKeyFromClipboard(Context context, KeyStore keystore, String password) throws Exception {

        if (keystore.getAccounts().size() > 0) {
            keystore.deleteAccount(keystore.getAccounts().get(0), password);
        }
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        byte[] key = clipboard.getPrimaryClip().getItemAt(0).getText().toString().getBytes();
        keystore.importKey(key, password, password);
    }

}
