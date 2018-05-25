package com.blockchain.store.playmarket.ui.fingerprint_screen;

import android.content.Context;

public class FingerprintConfiguringContract {

    interface View{
        void openWelcomeActivity(String address);

        void openMainActivity();

        void closeFingerprintActivity();

        void showToast(String textResources);
    }

    interface Presenter{
        void init(View view, Context context);
        void encryptAccountPassword(String accountPassword);
    }

}
