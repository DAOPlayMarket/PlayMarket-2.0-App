package com.blockchain.store.playmarket.ui.my_ico_screen;

import com.blockchain.store.playmarket.data.entities.AppInfo;

import java.util.ArrayList;

public class MyIcoContract {
    interface View {
        void showProgress(boolean isShow);

        void onIcoAppsReady(ArrayList<AppInfo> apps);

        void onIcoAppsFailed(Throwable throwable);
    }

    interface Presenter {
        void init(View v);

        void getMyIcoApps();
    }
}
