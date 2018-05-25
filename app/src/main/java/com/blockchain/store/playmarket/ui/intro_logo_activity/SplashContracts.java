package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class SplashContracts {
    public interface View {

        void onLocationReady();

        void setStatusText(@StringRes int stringRes);
        void setStatusText(@StringRes int stringRes, String errorString);
        void setStatusText(String stringRes);

        void onNearestNodeFailed(Throwable throwable);
    }

    public interface Presenter {
        void init(View view);

        void requestUserLocation(Context context);
    }
}