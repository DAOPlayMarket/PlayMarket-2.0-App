package com.blockchain.store.playmarket.ui.my_apps_screen;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.blockchain.store.playmarket.utilities.MyPackageManager;

import java.util.List;

public class MyAppsPresenter implements MyAppsContract.Presenter {
    private MyAppsContract.View view;

    @Override
    public void init(MyAppsContract.View view) {
        this.view = view;
    }

    public void getApps() {
        List<ApplicationInfo> allInstalledApps = MyPackageManager.getAllInstalledApps();
    }
}
