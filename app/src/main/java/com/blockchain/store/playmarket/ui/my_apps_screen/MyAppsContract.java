package com.blockchain.store.playmarket.ui.my_apps_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppLibrary;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

public class MyAppsContract {
    public interface View {


        void showLoading(boolean isShow);

        void onAppsReady(ArrayList<AppLibrary> appLibraries, Integer second);

        void onAppsFailed(Throwable throwable);

        void updateApp(App app, int progress, Constants.APP_STATE stateDownloading);
    }

    public interface Presenter {
        void init(View view);

    }
}
