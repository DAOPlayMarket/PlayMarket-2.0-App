package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;
import android.location.Location;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class SplashContracts {
    public interface View {

        void onLocationReady(Location location);
    }

    public interface Presenter {
        void init(View view);

        void requestUserLocation(Context context);
    }
}