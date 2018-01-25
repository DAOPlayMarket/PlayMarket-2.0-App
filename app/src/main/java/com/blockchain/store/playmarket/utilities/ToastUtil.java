package com.blockchain.store.playmarket.utilities;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.blockchain.store.playmarket.Application;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class ToastUtil {
    private static Toast toast;
    private static Context context;

    public static void showToast(String msg) {
        toast = new Toast(context);
        toast.setText(msg);
        show();
    }

    public static void showToast(@StringRes int msg) {
        toast = new Toast(context);
        toast.setText(msg);
        show();
    }

    private static void show() {
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void setContext(Context context) {
        ToastUtil.context = context;
    }
}
