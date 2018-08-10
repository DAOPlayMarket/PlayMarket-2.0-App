package com.blockchain.store.playmarket.ui.my_apps_screen;

import com.blockchain.store.playmarket.data.entities.AppLibrary;

import java.util.ArrayList;

public class MyAppsContract {
    public interface View {


        void showLoading(boolean isShow);

        void onAppsReady(ArrayList<AppLibrary> appLibraries);

        void onAppsFailed(Throwable throwable);
    }

    public interface Presenter {
        void init(View view);

    }
}
