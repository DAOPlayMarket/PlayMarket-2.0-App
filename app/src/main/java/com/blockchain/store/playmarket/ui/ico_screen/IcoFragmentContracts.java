package com.blockchain.store.playmarket.ui.ico_screen;

import com.blockchain.store.playmarket.data.entities.App;

import java.util.ArrayList;

public class IcoFragmentContracts {
    public interface View {

        void onIcoAppsReady(ArrayList<App> apps);

        void onIcoAppsFailed(Throwable throwable);

        void setProgress(boolean progress);
    }

    public interface Presenter {
        void init(View view);

        void getIcoApps();
    }
}
