package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class SplashContracts {
    public interface View {

        void onLocationReady();
    }

    public interface Presenter {
        void init(View view);

        void requestUserLocation(Context context);
    }
}